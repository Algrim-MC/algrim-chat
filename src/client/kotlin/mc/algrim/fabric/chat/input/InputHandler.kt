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

import fi.dy.masa.malilib.hotkeys.IKeybindManager
import fi.dy.masa.malilib.hotkeys.IKeybindProvider
import mc.algrim.fabric.chat.AlgrimChatClient.MOD_NAME
import mc.algrim.fabric.chat.config.Config

object InputHandler : IKeybindProvider {

    override fun addKeysToMap(manager: IKeybindManager) {
        manager.addKeybindToMap(Config.globalConfig.configGuiHotKey.keybind)
    }

    override fun addHotkeys(manager: IKeybindManager) {
        manager.addHotkeysForCategory(MOD_NAME, "Chat", listOf(Config.globalConfig.configGuiHotKey))
    }
}
