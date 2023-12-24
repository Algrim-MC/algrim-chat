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

package mc.algrim.fabric.chat

import fi.dy.masa.malilib.gui.GuiBase
import fi.dy.masa.malilib.hotkeys.*
import mc.algrim.fabric.chat.AlgrimChat.MOD_NAME
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.gui.ConfigGui
import mc.algrim.fabric.chat.gui.NewPatternGui
import mc.algrim.fabric.chat.gui.data.ListItemData

object HotkeyHandler : IKeybindProvider, IHotkeyCallback {

    init {
        Config.globalConfig.configGuiHotKey.keybind.setCallback(this)
        Config.globalConfig.debugGuiHotKey.keybind.setCallback(this)
    }

    override fun addKeysToMap(manager: IKeybindManager) {
        manager.addKeybindToMap(Config.globalConfig.configGuiHotKey.keybind)
        manager.addKeybindToMap(Config.globalConfig.debugGuiHotKey.keybind)
    }

    override fun addHotkeys(manager: IKeybindManager) {
        manager.addHotkeysForCategory(
            MOD_NAME,
            "Chat",
            listOf(Config.globalConfig.configGuiHotKey, Config.globalConfig.debugGuiHotKey)
        )
    }

    override fun onKeyAction(action: KeyAction?, key: IKeybind?): Boolean {
        return when (key) {
            Config.globalConfig.configGuiHotKey.keybind -> {
                GuiBase.openGui(ConfigGui())
                true
            }

            Config.globalConfig.debugGuiHotKey.keybind -> {
                GuiBase.openGui(NewPatternGui(scope = ListItemData.Scope.GLOBAL, patternIndex = 0))
                true
            }

            else -> false
        }
    }
}
