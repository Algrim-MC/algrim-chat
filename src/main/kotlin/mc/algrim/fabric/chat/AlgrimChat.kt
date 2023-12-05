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

import fi.dy.masa.malilib.config.ConfigManager
import fi.dy.masa.malilib.event.InitializationHandler
import fi.dy.masa.malilib.event.InputEventHandler
import fi.dy.masa.malilib.event.WorldLoadHandler
import fi.dy.masa.malilib.interfaces.IInitializationHandler
import mc.algrim.fabric.chat.config.Config
import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory


object AlgrimChat : ClientModInitializer, IInitializationHandler {
    const val MOD_ID = "algrim-chat"
    const val MOD_NAME = "Algrim Chat"

    //chat-sed

    @JvmStatic
    val logger = LoggerFactory.getLogger(MOD_ID)!!

    override fun onInitializeClient() {
        InitializationHandler.getInstance().registerInitializationHandler(this)
    }

    override fun registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Config.globalConfig)
        InputEventHandler.getKeybindManager().registerKeybindProvider(HotkeyHandler)

        WorldLoadHandler.getInstance().registerWorldLoadPreHandler(Config)
        WorldLoadHandler.getInstance().registerWorldLoadPostHandler(Config)
    }
}
