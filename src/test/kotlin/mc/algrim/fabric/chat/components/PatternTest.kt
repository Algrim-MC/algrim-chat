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

import kotlin.test.Test
import kotlin.test.assertEquals

class PatternTest {
    @Test
    fun testPattern() {
        val patternPartsValue = patternValueFromString("This is \\[ a [green](test) /pa(t)+ern/")
        val expectedPartsValue = "This is [ a |test| |pa(t)+ern"

        println(patternPartsValue)
        assertEquals(expectedPartsValue, patternPartsValue)
    }

    @Test
    fun testVanillaChatPattern() {
        val patternPartsValue = patternValueFromString("<[#0000FF](/[0-Z]+/)> [#FF0000](/.*/)")
        val expectedPartsValue = "<|[0-Z]+|> |.*"

        assertEquals(expectedPartsValue, patternPartsValue)
    }

    private fun patternValueFromString(patternStr: String) =
        Pattern.fromString(patternStr).parts.joinToString("|") { it.value }
}
