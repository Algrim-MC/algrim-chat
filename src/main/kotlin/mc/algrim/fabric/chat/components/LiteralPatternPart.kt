/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2023-2024.
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


class LiteralPatternPart(
    override val pattern: String,
    override val index: Int = 0,
    vararg untilChars: Char
) : PatternPart {
    override val length: Int
    override val value: String
    override fun matches(string: String) = value == string

    init {
        val remainder = pattern.substring(index)
        val stringLiteralBuilder = StringBuilder(remainder.length)
        var escapeCharCount = 0

        for ((i, char) in remainder.toCharArray().withIndex()) {
            val escaped = if (i > 0) remainder[i - 1] == '\\' else false

            if (!escaped && char == '\\') {
                escapeCharCount++
                continue
            }

            if (!escaped && untilChars.contains(char)) {
                break
            }

            stringLiteralBuilder.append(char)
        }

        this.value = stringLiteralBuilder.toString()
        this.length = this.value.length + escapeCharCount
    }

    override fun apply(messageContent: String, startIndex: Int): String? {
        val endIndex = startIndex + value.length

        return if (messageContent.length < endIndex || !matches(messageContent.substring(startIndex, endIndex))) {
            null
        } else {
            value
        }
    }

    override fun toCharStylePair(messageContent: String, startIndex: Int): Array<Pair<Char, Style>>? {
        return apply(messageContent, startIndex)?.map { it to Style.EMPTY }?.toTypedArray()
    }

    companion object : PatternPart.Delimiters {
        override val openChar = null
        override val closeChar = null
    }
}
