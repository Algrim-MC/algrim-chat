/*
 * This file is part of algrim-chat, a chat styling fabric mod.
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

interface PatternPart {
    val pattern: String
    val index: Int
    val value: String
    val length: Int
    fun matches(string: String): Boolean
    fun apply(messageContent: String, startIndex: Int): String?
    fun toCharStylePair(messageContent: String, startIndex: Int): Array<Pair<Char, Style>>?

    interface Parentheses {
        val openChar: Char?
        val closeChar: Char?
    }
}
