/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2023-2025.
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

package mc.algrim.fabric.chat.gui

import fi.dy.masa.malilib.gui.GuiBase
import mc.algrim.fabric.chat.AlgrimChat
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextWidget

open class AlgrimGuiBase : GuiBase() {
    private val textWidgets = mutableListOf<TextWidget>()

    fun addTextWidget(textWidget: TextWidget): TextWidget {
        textWidgets.add(textWidget)
        AlgrimChat.logger.info("Added text")
        return textWidget
    }

    fun clearTextWidgets() {
        textWidgets.clear()
    }

    fun removeTextWidget(textWidget: TextWidget): Boolean {
        return textWidgets.remove(textWidget)
    }

    override fun clearElements() {
        super.clearElements()
        clearTextWidgets()
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.render(drawContext, mouseX, mouseY, partialTicks)
        for (textWidget in textWidgets) {
            drawContext.drawText(this.textRenderer, textWidget.message, textWidget.x, textWidget.y, 0xFFFFFF, true)
        }
    }
}
