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

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric
import net.minecraft.client.font.TextRenderer

class GuiTextField(x: Int, y: Int, width: Int, height: Int, textRenderer: TextRenderer?) :
    GuiTextFieldGeneric(x, y, width, height, textRenderer) {

    // Workaround for field not being unfocused.
    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        val ret = super.mouseClicked(mouseX, mouseY, mouseButton)

        return if (!this.isMouseOver(mouseX.toInt(), mouseY.toInt())) {
            this.isFocused = false
            true
        } else ret
    }
}
