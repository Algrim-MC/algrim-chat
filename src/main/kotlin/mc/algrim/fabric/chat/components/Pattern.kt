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


class Pattern(val parts: Array<PatternPart>) {

    fun applyTransforms(content: String): String? {
        var i = 0
        val transformedPattern = StringBuilder()

        for (part in parts) {
            val transformedPart = part.apply(content, i)
            transformedPart ?: return null

            transformedPattern.append(transformedPart)
            i = transformedPart.length
        }

        return transformedPattern.toString()
    }

    fun toCharStylePair(messageContent: String): Array<Pair<Char, Style>>? {
        val charStylePairs = ArrayList<Pair<Char, Style>>(messageContent.length)

        var i = 0
        for (part in parts) {
            val p = part.toCharStylePair(messageContent, i) ?: return null
            charStylePairs.addAll(p)

            i += p.size
        }

        return charStylePairs.toTypedArray()
    }

    companion object {

        fun fromString(patternString: String): Pattern {
            val parts = mutableListOf<PatternPart>()
            var i = 0
            var loopCount = 0

            while (i < patternString.length) {
                if (++loopCount > 2500) throw IllegalArgumentException("Pattern has exceeded the maximum number of iterations")

                val char = patternString[i]
                val escaped = if (i > 0) patternString[i - 1] == '\\' else false

                if (!escaped) {
                    val part: PatternPart = when (char) {
                        StylePatternPart.openChar -> StylePatternPart(patternString, i)
                        RegexPatternPart.openChar -> RegexPatternPart(patternString, i)
                        else -> LiteralPatternPart(
                            patternString,
                            i,
                            StylePatternPart.openChar,
                            RegexPatternPart.openChar
                        )
                    }

                    parts.add(part)
                    if (part.length == 0) i++ else i += part.length
                } else i++
            }

            return Pattern(parts.toTypedArray())
        }
    }
}
