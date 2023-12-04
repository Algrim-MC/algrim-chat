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

package mc.algrim.fabric.chat

import fi.dy.masa.malilib.event.InitializationHandler
import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory


object AlgrimChat : ClientModInitializer {
    const val MOD_ID = "algrim-chat"
    const val MOD_NAME = "AlgrimChat"

    //chat-sed

    @JvmStatic
    val logger = LoggerFactory.getLogger(MOD_ID)!!

    override fun onInitializeClient() {
        InitializationHandler.getInstance().registerInitializationHandler(InitHandler())
    }
}
