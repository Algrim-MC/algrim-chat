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

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fi.dy.masa.malilib.config.ConfigUtils
import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.IConfigHandler
import fi.dy.masa.malilib.util.JsonUtils
import mc.algrim.fabric.chat.AlgrimChat
import mc.algrim.fabric.chat.config.option.PatternOption
import java.io.File

abstract class ConfigFile(val configFile: File) : IConfigHandler {
    open val version: Int = 2
    abstract val categories: Map<String, List<IConfigBase>>

    override fun load() {
        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            val element: JsonElement? = JsonUtils.parseJsonFile(configFile)
            if (element != null && element.isJsonObject) {
                var root = element.asJsonObject

                val versionJson = root.getAsJsonPrimitive("version")
                val fileVersion: Int =
                    if (versionJson == null || !versionJson.isNumber) -1 else versionJson.asNumber.toInt()

                if (fileVersion < this.version) {
                    root = handleUpdate(fileVersion, root)
                }

                for ((category, options) in categories) {
                    ConfigUtils.readConfigBase(root, category, options)
                }
            }
        }
    }

    override fun save() {
        AlgrimChat.logger.info("Saving config to $configFile")
        if (configFile.parentFile.exists() && configFile.parentFile.isDirectory() || configFile.parentFile.mkdirs()) {
            val root = JsonObject()

            for ((category, options) in categories) {
                ConfigUtils.writeConfigBase(root, category, options)
            }

            root.addProperty("version", this.version)

            JsonUtils.writeJsonToFile(root, configFile)
        }
    }

    open fun handleUpdate(fileVersion: Int, root: JsonObject): JsonObject {
        if (fileVersion < 2) {
            val chatObj = JsonUtils.getNestedObject(root, "Chat", false) ?: return root
            val patternsJson = chatObj.getAsJsonArray("patterns")
            val newPatternsJson = JsonArray()

            for (patternJson in patternsJson) {
                if (!patternJson.isJsonPrimitive) continue
                val patternOption = JsonObject()
                val patternVal = patternJson.asString

                patternOption.addProperty(PatternOption.PropertyName.NAME.jsonName, "")
                patternOption.addProperty(PatternOption.PropertyName.PATTERN.jsonName, patternVal)
                patternOption.addProperty(PatternOption.PropertyName.ENABLED.jsonName, true)

                newPatternsJson.add(patternOption)
            }

            chatObj.add("patterns", newPatternsJson)
        }

        return root
    }
}
