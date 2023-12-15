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

import net.minecraft.text.TextColor
import net.minecraft.text.Style as MinecraftStyle

object StyleUtils {
    val strippedStyle: MinecraftStyle = MinecraftStyle.EMPTY
        .withBold(false)
        .withItalic(false)
        .withUnderline(false)
        .withStrikethrough(false)
        .withObfuscated(false)

    fun styleFromString(string: String, defaultStyle: MinecraftStyle = MinecraftStyle.EMPTY): MinecraftStyle {
        var style = defaultStyle

        val args = string.split(",")
            .map { it.trim().lowercase() }
            .filter {
                if (it == "!") {
                    style = strippedStyle
                    false
                } else {
                    true
                }
            }

        for (arg in args) {
            val opt = !arg.startsWith("!")

            style = when (if (opt) arg else arg.substring(1)) {
                "bold", "b" -> style.withBold(opt)
                "italic", "it", "i" -> style.withItalic(opt)
                "underline", "u" -> style.withUnderline(opt)
                "strikethrough", "s" -> style.withStrikethrough(opt)
                "obfuscate", "o" -> style.withObfuscated(opt)
                else -> TextColor.parse(arg)
                    .map { style.withColor(it) }
                    .result()
                    .orElse(MinecraftStyle.EMPTY)
            }
        }

        return style
    }
}
