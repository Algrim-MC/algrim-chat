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

package mc.algrim.fabric.chat.config

import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.options.ConfigBoolean
import fi.dy.masa.malilib.config.options.ConfigHotkey
import mc.algrim.fabric.chat.config.option.PatternOption
import java.io.File

class GlobalConfigFile(configFile: File) : ConfigFile(configFile) {
    val enabled = ConfigBoolean("enabled", true, "Enable chat styling")
    val patterns = PatternOption("patterns", "Style patterns to be used globally")
    val configGuiHotKey = ConfigHotkey("openConfigGui", "R,C", "The hotkey used to open the config gui")

    val debugGuiHotKey = ConfigHotkey("openDebugGui", "", "The hotkey used to open the debug gui")

    val guiOptions = listOf<IConfigBase>(enabled, configGuiHotKey, debugGuiHotKey)
    val chatOptions = listOf<IConfigBase>(enabled, configGuiHotKey, debugGuiHotKey, patterns)

    override val categories = mapOf("Chat" to chatOptions)
}
