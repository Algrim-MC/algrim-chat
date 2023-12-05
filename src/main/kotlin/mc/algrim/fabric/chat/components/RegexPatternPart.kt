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

package mc.algrim.fabric.chat.components

import net.minecraft.text.Style

class RegexPatternPart(override val pattern: String, override val index: Int) : PatternPart {
    override val value: String
    override val length: Int

    val regex: Regex
    override fun matches(string: String) = pattern.matches(regex)

    init {
        val remainder = pattern.substring(index)
        var partLength = 0

        for ((i, char) in remainder.toCharArray().withIndex()) {
            if (i == 0) {
                if (char != openChar) throw IllegalArgumentException("Pattern '$pattern', invalid open char, should be '$openChar'")
                else continue
            }

            val escaped = if (i > 0) remainder[i - 1] == '\\' else false

            if (!escaped && char == closeChar) {
                partLength = i + 1
                break
            }

            if (i == remainder.length - 1) {
                throw IllegalArgumentException("Pattern '$pattern', is invalid. Unexpected end of pattern.")
            }
        }

        this.length = partLength
        this.value = remainder.substring(1, length - 1)
        this.regex = value.toRegex()
    }

    override fun apply(messageContent: String, startIndex: Int): String? {
        val rx = regex.matchAt(messageContent, startIndex)
        return rx?.value
    }

    override fun toCharStylePair(messageContent: String, startIndex: Int): Array<Pair<Char, Style>>? {
        return apply(messageContent, startIndex)?.map { it to Style.EMPTY }?.toTypedArray()
    }

    companion object : PatternPart.Delimiters {
        override val openChar = '/'
        override val closeChar = '/'
    }
}
