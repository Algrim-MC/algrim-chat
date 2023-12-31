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
import mc.algrim.fabric.chat.config.option.PatternOption
import java.io.File

class ServerConfigFile(configFile: File) : ConfigFile(configFile) {
    val patterns = PatternOption("patterns", "Style patterns for chat on this server.")
    val useGlobal = ConfigBoolean("useGlobal", true, "Whether global patterns should be used on this server.")

    val guiOptions = listOf<IConfigBase>(useGlobal)
    val chatOptions = listOf<IConfigBase>(useGlobal, patterns)

    override val categories: Map<String, List<IConfigBase>> = mapOf("Chat" to chatOptions)

    init {
        // Loading manually when joining server.
        this.load()
    }
}
