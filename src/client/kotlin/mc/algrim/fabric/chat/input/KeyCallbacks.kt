/*
 * This file is part of algrim-chat, a chat styling fabric mod.
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

package mc.algrim.fabric.chat.input

import fi.dy.masa.malilib.gui.GuiBase
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback
import fi.dy.masa.malilib.hotkeys.IKeybind
import fi.dy.masa.malilib.hotkeys.KeyAction
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.gui.ConfigGui

object KeyCallbacks : IHotkeyCallback {

    init {
        Config.globalConfig.configGuiHotKey.keybind.setCallback(this)
    }

    override fun onKeyAction(action: KeyAction, key: IKeybind): Boolean {
        return if (key == Config.globalConfig.configGuiHotKey.keybind) {
            GuiBase.openGui(ConfigGui())
            true
        } else false
    }
}
