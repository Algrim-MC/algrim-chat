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
import mc.algrim.fabric.chat.gui.PatternListGui
import net.minecraft.client.gui.DrawContext
import kotlin.math.max
import kotlin.math.min

class PatternListItemWidget(
    x: Int,
    y: Int,
    width: Int,
    private val patternWrapper: PatternListGui.PatternWrapper,
    listIndex: Int,
    private val eventHandler: ((listItem: PatternListItemWidget, patternWrapper: PatternListGui.PatternWrapper, buttonId: ButtonId) -> Unit)? = null
) : WidgetListEntryBase<PatternListGui.PatternWrapper>(x, y, width, 24, patternWrapper, listIndex) {

    init {
        val label = createLabel(x + 10, y - 4 + this.height / 2, 0xFFFFFF, patternWrapper.value)

        if (patternWrapper.type == PatternListGui.PatternWrapper.Type.SEPARATOR) {
            label.x += (this.width - label.width) / 2
        }

        val buttons: List<ButtonId> = when (patternWrapper.type) {
            PatternListGui.PatternWrapper.Type.PATTERN -> ButtonId.entries.reversed()
            PatternListGui.PatternWrapper.Type.EMPTY_SCOPE -> listOf(ButtonId.ADD)
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

    private fun createLabel(x: Int, y: Int, colour: Int, text: String): WidgetLabel {
        val width = min(this.getStringWidth(text) + 10, this.width - 20)
        val label = WidgetLabel(x, y, width, 10, colour, text)

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
        eventHandler?.invoke(this, patternWrapper, buttonId)
    }

    override fun render(mouseX: Int, mouseY: Int, selected: Boolean, drawContext: DrawContext?) {
        RenderUtils.drawRect(
            this.x, this.y, this.width - 10, this.height, (if (listIndex % 2 == 0) 0xA0101010 else 0xA0303030).toInt()
        )
        super.render(mouseX, mouseY, selected, drawContext)
    }

    enum class ButtonId(val displayName: String) {
        EDIT("Edit"),
        ADD("+"),
        MOVE_UP("▲"),
        MOVE_DOWN("▼"),
        REMOVE("-")
    }
}
