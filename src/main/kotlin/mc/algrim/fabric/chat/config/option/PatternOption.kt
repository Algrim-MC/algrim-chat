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

package mc.algrim.fabric.chat.config.option

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fi.dy.masa.malilib.config.ConfigType
import fi.dy.masa.malilib.config.options.ConfigBase
import mc.algrim.fabric.chat.AlgrimChat

class PatternOption(name: String?, comment: String?, prettyName: String? = null) :
    ConfigBase<PatternOption>(ConfigType.STRING_LIST, name, comment, prettyName) {

    var patternOptionValues = listOf<PatternOptionValue>()
        set(value) {
            field = value
            this.onValueChanged()
        }

    override fun setValueFromJsonElement(element: JsonElement) {
        if (!element.isJsonArray) {
            AlgrimChat.logger.error("Failed to deserialize option '${this.name}', '$element' is not an array!")
            return
        }

        val root = element.asJsonArray

        val values = mutableListOf<PatternOptionValue>()

        for (rootElement in root) {
            try {
                val optionObj = rootElement.asJsonObject

                val name = optionObj.getAsJsonPrimitive(PropertyName.NAME.jsonName).asString
                val patternValue = optionObj.getAsJsonPrimitive(PropertyName.PATTERN.jsonName).asString
                val enabled = optionObj.getAsJsonPrimitive(PropertyName.ENABLED.jsonName).asBoolean

                values.add(PatternOptionValue(name, patternValue, enabled))
            } catch (e: IllegalStateException) {
                AlgrimChat.logger.error("Failed to deserialize pattern value in option '${this.name}', '$rootElement'!")
            }
        }

        patternOptionValues = values
    }

    override fun getAsJsonElement(): JsonElement {
        val root = JsonArray()

        for (optionValue in patternOptionValues) {
            val optionObj = JsonObject()

            optionObj.addProperty(PropertyName.NAME.jsonName, optionValue.name)
            optionObj.addProperty(PropertyName.PATTERN.jsonName, optionValue.patternValue)
            optionObj.addProperty(PropertyName.ENABLED.jsonName, optionValue.enabled)

            root.add(optionObj)
        }

        return root
    }

    override fun isModified(): Boolean {
        return patternOptionValues.isNotEmpty()
    }

    override fun resetToDefault() {
        patternOptionValues = listOf()
    }

    class PatternOptionValue(val name: String, val patternValue: String, val enabled: Boolean) {
        fun withEnabled(enabled: Boolean): PatternOptionValue {
            return PatternOptionValue(name, patternValue, enabled)
        }
    }

    enum class PropertyName(val jsonName: String) {
        NAME("name"),
        PATTERN("pattern"),
        ENABLED("enabled")
    }
}
