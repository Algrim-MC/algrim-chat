/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2023-2024.
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
import mc.algrim.fabric.chat.gui.data.EmptyEntryData
import mc.algrim.fabric.chat.gui.data.PatternEntryData
import mc.algrim.fabric.chat.gui.data.PatternListEntryData
import mc.algrim.fabric.chat.gui.data.SeparatorEntryData
import net.minecraft.client.gui.DrawContext
import kotlin.math.max

class PatternListEntryWidget(
    x: Int,
    y: Int,
    width: Int,
    entry: PatternListEntryData,
    listIndex: Int,
    private val nameColumnWidth: Int,
    private val eventHandler: ((listItem: PatternListEntryWidget, patternListEntryData: PatternListEntryData, buttonId: ButtonId) -> Unit)? = null
) : WidgetListEntryBase<PatternListEntryData>(x, y, width, 24, entry, listIndex) {

    init {
        when (entry) {
            is PatternEntryData -> initPatternEntry(entry)
            is SeparatorEntryData -> initSeparatorEntry(entry)
            is EmptyEntryData -> initEmptyEntry()
        }
    }

    private fun initPatternEntry(patternEntry: PatternEntryData) {
        var labelX = x + 10

        val nameLabel = createLabel(
            labelX,
            y - 4 + this.height / 2,
            nameColumnWidth,
            0xFFFFFF,
            patternEntry.value.name
        )
        labelX += nameLabel.width + 10

        createLabel(
            labelX,
            y - 4 + this.height / 2,
            this.width - nameColumnWidth - 190,
            0xFFFFFF,
            patternEntry.value.patternValue
        )

        var buttonX = this.width
        val buttons = ButtonId.entries.filter {
            !(it == ButtonId.ENABLE && patternEntry.value.enabled
                || it == ButtonId.DISABLE && !patternEntry.value.enabled)
        }.reversed()

        for (buttonId in buttons) {
            val button =
                createButton(buttonX, y, buttonId.displayName) { _: ButtonBase, _: Int -> executeButton(buttonId) }
            button.x -= button.width + 2
            buttonX -= button.width + 2
        }
    }

    private fun initSeparatorEntry(separatorEntry: SeparatorEntryData) {
        val labelX = x + 10
        val label = createLabel(labelX, y - 4 + this.height / 2, this.width, 0xFFFFFF, separatorEntry.value)

        label.setCentered(true)
    }

    private fun initEmptyEntry() {
        val buttonX = this.width
        val addButtonId = ButtonId.ADD
        val button =
            createButton(buttonX, y, addButtonId.displayName) { _: ButtonBase, _: Int -> executeButton(addButtonId) }

        button.x -= button.width + 2
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
        eventHandler?.invoke(this, entry!!, buttonId)
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
