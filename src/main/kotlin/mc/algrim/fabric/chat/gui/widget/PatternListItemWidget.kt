/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mc.algrim.fabric.chat.gui.widget

import fi.dy.masa.malilib.gui.button.ButtonBase
import fi.dy.masa.malilib.gui.button.ButtonGeneric
import fi.dy.masa.malilib.gui.button.IButtonActionListener
import fi.dy.masa.malilib.gui.widgets.WidgetLabel
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase
import fi.dy.masa.malilib.render.RenderUtils
import mc.algrim.fabric.chat.gui.data.ListItemData
import mc.algrim.fabric.chat.gui.data.PatternListItemData
import mc.algrim.fabric.chat.gui.data.SeparatorItemData
import net.minecraft.client.gui.DrawContext
import kotlin.math.max
import kotlin.math.min

class PatternListItemWidget(
    x: Int,
    y: Int,
    width: Int,
    private val listItemData: ListItemData,
    listIndex: Int,
    private val eventHandler: ((listItem: PatternListItemWidget, listItemData: ListItemData, buttonId: ButtonId) -> Unit)? = null
) : WidgetListEntryBase<ListItemData>(x, y, width, 24, listItemData, listIndex) {

    init {
        var labelX = x + 10
        when (listItemData) {
            is PatternListItemData -> {
                val labelWidth = ((this.width - 20) * 0.20).toInt()
                val nameLabel = createLabel(
                    labelX,
                    y - 4 + this.height / 2,
                    labelWidth,
                    0xFFFFFF,
                    listItemData.value.name
                )
                labelX += nameLabel.width + 10
                createLabel(
                    labelX,
                    y - 4 + this.height / 2,
                    this.width - labelWidth - 200,
                    0xFFFFFF,
                    listItemData.value.patternValue
                )
            }

            is SeparatorItemData -> {
                val valueLabelSize = min(this.getStringWidth(listItemData.value) + 10, this.width - 20)
                val valueLabel =
                    createLabel(labelX, y - 4 + this.height / 2, valueLabelSize, 0xFFFFFF, listItemData.value)
                valueLabel.x += (this.width - valueLabel.width) / 2
            }
        }

        val buttons: List<ButtonId> = when (listItemData.type) {
            ListItemData.Type.PATTERN -> {
                if (listItemData !is PatternListItemData) listOf()
                else ButtonId.entries.filter {
                    !(it == ButtonId.ENABLE && listItemData.value.enabled
                        || it == ButtonId.DISABLE && !listItemData.value.enabled)
                }.reversed()
            }

            ListItemData.Type.EMPTY_SCOPE -> listOf(ButtonId.ADD)
            else -> listOf()
        }

        var buttonX = this.width - 10
        for (buttonId in buttons) {
            val newButton =
                createButton(buttonX, y, buttonId.displayName) { _: ButtonBase, _: Int -> executeButton(buttonId) }
            newButton.x -= newButton.width + 2
            buttonX -= newButton.width + 2
        }
    }

    private fun createLabel(x: Int, y: Int, width: Int, colour: Int, text: String): WidgetLabel {
        val textWidth = this.getStringWidth(text)
        val labelValue = if (textWidth > width) {
            this.textRenderer.trimToWidth(text, width - 10) + "…"
        } else text

        val label = WidgetLabel(x, y, width, 10, colour, labelValue)

        addWidget(label)

        return label
    }

    private fun createButton(x: Int, y: Int, displayName: String, listener: IButtonActionListener): ButtonGeneric {
        val width = max(this.getStringWidth(displayName) + 10, this.height - 2)
        val button = ButtonGeneric(x, y + 2, width, this.height - 4, displayName)

        this.addButton(button, listener)

        return button
    }

    private fun executeButton(buttonId: ButtonId) {
        eventHandler?.invoke(this, listItemData, buttonId)
    }

    override fun render(mouseX: Int, mouseY: Int, selected: Boolean, drawContext: DrawContext?) {
        RenderUtils.drawRect(
            this.x, this.y, this.width - 10, this.height, (if (listIndex % 2 == 0) 0xA0101010 else 0xA0303030).toInt()
        )
        super.render(mouseX, mouseY, selected, drawContext)
    }

    enum class ButtonId(val displayName: String) {
        ENABLE("☐"),
        DISABLE("☑"),
        EDIT("Edit"),
        ADD("+"),
        MOVE_UP("▲"),
        MOVE_DOWN("▼"),
        REMOVE("-")
    }
}
