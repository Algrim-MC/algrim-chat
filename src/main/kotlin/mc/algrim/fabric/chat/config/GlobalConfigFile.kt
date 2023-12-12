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

import com.google.common.collect.ImmutableList
import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.options.ConfigBoolean
import fi.dy.masa.malilib.config.options.ConfigHotkey
import fi.dy.masa.malilib.config.options.ConfigStringList
import java.io.File

class GlobalConfigFile(configFile: File) : ConfigFile(configFile) {
    val enabled = ConfigBoolean("enabled", true, "Enable chat styling")
    val patterns = ConfigStringList("patterns", ImmutableList.of(), "Style patterns to be used globally")
    val configGuiHotKey = ConfigHotkey("openConfigGui", "R,C", "The hotkey used to open the config gui")

    val debugGuiHotKey = ConfigHotkey("openDebugGui", "", "The hotkey used to open the debug gui")

    val chatOptions = listOf<IConfigBase>(enabled, configGuiHotKey, debugGuiHotKey, patterns)

    override val categories = mapOf("Chat" to chatOptions)
}
