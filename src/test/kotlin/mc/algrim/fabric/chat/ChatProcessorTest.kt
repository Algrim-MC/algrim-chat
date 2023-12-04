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

package mc.algrim.fabric.chat

import mc.algrim.fabric.chat.components.Pattern
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import java.util.*
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ChatProcessorTest {
    @Test
    fun test() {
        // Some sample messages
        // [MOD] Barry: Stuff a mod named Barry would say.
        // Carl: Stuff a player named Barry would say.
        val testMsg =
            Text.literal("[MOD]").append(" ").append("Barry").append(": ").append("Stuff a mod named Barry would say.")
        val testMsg2 = Text.literal("Carl").append(": ").append("Stuff a player named Carl would say.")

        val testPattern = Pattern.fromString("[#821212](/(\\\\[MOD] \\)?/)[!,#00FFFF](/\\\\w+/): [!,white](/.*/)")
        ChatProcessor.enabled = true
        ChatProcessor.patterns.add(testPattern)

        val styledTestMsg = ChatProcessor.execute(testMsg)
        val styledTestMsg2 = ChatProcessor.execute(testMsg2)

        val expectedTestMsgStyles = listOf(
            "[MOD] " to Style.EMPTY.withColor(TextColor.parse("#821212")),
            "Barry" to StyleUtils.strippedStyle.withColor(TextColor.parse("#00FFFF")),
            ": " to Style.EMPTY,
            "Stuff a mod named Barry would say." to StyleUtils.strippedStyle.withColor(TextColor.parse("white"))
        )

        val expectedTestMsg2Styles = listOf(
            "Carl" to Style.EMPTY.withColor(TextColor.parse("#00FFFF")),
            ": " to Style.EMPTY,
            "Stuff a player named Carl would say." to Style.EMPTY.withColor(TextColor.parse("white"))
        )

        val actualTestMsgStyles = mutableListOf<Pair<String, Style>>()
        val actualTestMsg2Styles = mutableListOf<Pair<String, Style>>()

        styledTestMsg.visit({ style: Style, content: String ->
            println("$content $style")
            actualTestMsgStyles.add(content to style)
            Optional.empty<Any?>()
        }, Style.EMPTY)

        styledTestMsg2.visit({ style: Style, content: String ->
            actualTestMsg2Styles.add(content to style)
            Optional.empty<Any?>()
        }, Style.EMPTY)

        assertContentEquals(expectedTestMsgStyles, actualTestMsgStyles)
        assertContentEquals(expectedTestMsg2Styles, actualTestMsg2Styles)
    }
}
