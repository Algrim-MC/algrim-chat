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

package mc.algrim.fabric.chat

import mc.algrim.fabric.chat.components.Pattern
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.*
import kotlin.test.Test
import kotlin.test.assertContentEquals

@TestInstance(PER_CLASS)
class ChatProcessorTest {

    @BeforeAll
    fun setup() {
        ChatProcessor.enabled = true

        val testPattern = Pattern.fromString("[#821212](/(\\[MOD] )?/)[!,#00FFFF](/\\w+/): [!,white](/.*/)")
        ChatProcessor.patterns.add(testPattern)
    }

    @Test
    fun testMod() {
        // [MOD] Barry: Stuff a mod named Barry would say.
        val modMessage =
            Text.literal("[MOD]").append(" ").append("Barry").append(": ").append("Stuff a mod named Barry would say.")
        val styledModMessage = ChatProcessor.execute(modMessage)
        val actualModMsgStyles = mutableListOf<Pair<String, Style>>()

        val expectedModMsgStyles = listOf(
            "[MOD] " to Style.EMPTY.withColor(colourOrThrow("#821212")),
            "Barry" to StyleUtils.strippedStyle.withColor(colourOrThrow("#00FFFF"))
                .withParent(StyleUtils.strippedStyle),
            ": " to Style.EMPTY,
            "Stuff a mod named Barry would say." to StyleUtils.strippedStyle.withColor(colourOrThrow("white"))
                .withParent(StyleUtils.strippedStyle)
        )


        styledModMessage.visit({ style: Style, content: String ->
            println("$content $style")
            actualModMsgStyles.add(content to style)
            Optional.empty<Any?>()
        }, Style.EMPTY)

        assertContentEquals(expectedModMsgStyles, actualModMsgStyles)
    }

    @Test
    fun testPlayer() {
        // Carl: Stuff a player named Carl would say.
        val playerMsg = Text.literal("Carl").append(": ").append("Stuff a player named Carl would say.")
        val styledPlayerMsg = ChatProcessor.execute(playerMsg)
        val actualPlayMsgStyles = mutableListOf<Pair<String, Style>>()

        val expectedPlayerMsgStyles = listOf(
            "Carl" to Style.EMPTY.withColor(colourOrThrow("#00FFFF"))
                .withParent(StyleUtils.strippedStyle),
            ": " to Style.EMPTY,
            "Stuff a player named Carl would say." to Style.EMPTY.withColor(colourOrThrow("white"))
                .withParent(StyleUtils.strippedStyle)
        )


        styledPlayerMsg.visit({ style: Style, content: String ->
            actualPlayMsgStyles.add(content to style)
            Optional.empty<Any?>()
        }, Style.EMPTY)

        assertContentEquals(expectedPlayerMsgStyles, actualPlayMsgStyles)
    }

    private fun colourOrThrow(name: String) = TextColor.parse(name).orThrow
}
