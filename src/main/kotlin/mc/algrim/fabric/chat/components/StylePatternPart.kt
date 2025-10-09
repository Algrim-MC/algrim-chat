/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2023-2025.
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

package mc.algrim.fabric.chat.components

import mc.algrim.fabric.chat.StyleUtils
import net.minecraft.text.Style


class StylePatternPart(override val pattern: String, override val index: Int) : PatternPart {
    override val value: String
    override val length: Int
    val valuePattern: Pattern
    val style: String

    override fun matches(string: String): Boolean {
        return false
    }

    override fun apply(messageContent: String, startIndex: Int): String? {
        val forward = messageContent.substring(startIndex)
        return valuePattern.applyTransforms(forward)
    }

    override fun toCharStylePair(messageContent: String, startIndex: Int): Array<Pair<Char, Style>>? {
        return valuePattern.toCharStylePair(messageContent.substring(startIndex))
            ?.map { (char, style) -> char to style.withParent(StyleUtils.styleFromString(this.style)) }
            ?.toTypedArray()
    }

    init {
        val remainder = pattern.substring(index)
        val styleStringBuilder = StringBuilder()
        var styleFinished = false

        if (!remainder.startsWith(openChar)) {
            throw IllegalArgumentException("Pattern '$pattern', invalid open char, should be '$openChar'")
        }

        var i = 1
        while (i < remainder.length) {
            val char = remainder[i]
            val escaped = remainder[i - 1] == '\\'

            if (!escaped) {
                if (char == styleCloseChar) {
                    styleFinished = true
                    i++
                    break
                }

                styleStringBuilder.append(char)
            }

            i++
        }

        if (!styleFinished) {
            throw IllegalArgumentException("Pattern '$pattern', is invalid. No closing style declaration '$styleCloseChar'.")
        }

        if (i == remainder.length || remainder[i++] != valueOpenChar) {
            throw IllegalArgumentException("Pattern '$pattern', is invalid. Closing style declaration '$styleCloseChar', should be followed by '$valueOpenChar' @$i")
        }

        if (i == remainder.length) {
            throw IllegalArgumentException("Pattern '$pattern', is invalid. Unexpected end of pattern.")
        }

        this.valuePattern = Pattern.fromString(remainder.substring(i), valueCloseChar)
        i += this.valuePattern.parts.sumOf { it.length }


        if (i >= remainder.length || remainder[i++] != valueCloseChar) {
            throw IllegalArgumentException("Pattern '$pattern', is invalid. Unexpected end of pattern.")
        }

        this.value = valuePattern.parts.joinToString("") { it.value }
        this.style = styleStringBuilder.toString()
        this.length = i
    }

    companion object : PatternPart.Delimiters {
        val styleOpenChar = '['
        val styleCloseChar = ']'
        val valueOpenChar = '('
        val valueCloseChar = ')'
        override val openChar = styleOpenChar
        override val closeChar = valueCloseChar
    }
}
