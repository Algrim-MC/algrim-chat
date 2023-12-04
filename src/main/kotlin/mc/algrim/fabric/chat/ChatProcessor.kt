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

import mc.algrim.fabric.chat.AlgrimChat.logger
import mc.algrim.fabric.chat.components.Pattern
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.util.*

object ChatProcessor {
    var enabled = false
    val patterns = ArrayList<Pattern>()

    fun execute(message: Text): Text {
        if (!enabled) return message

        val messageContent = message.string
        val messageCSPs = messageToCharStylePair(message)

        patterns.forEach { pattern ->
            val patternCSPs = pattern.toCharStylePair(messageContent)

            if (patternCSPs == null
                || patternCSPs.size != messageCSPs.size
                || !applyPatternCSP(messageCSPs, patternCSPs)
            ) {
                logger.debug(
                    "Pattern '{}'{} didn't match '{}'{}",
                    pattern,
                    patternCSPs?.size,
                    messageContent,
                    messageCSPs.size
                )

                return@forEach
            }

            return aggregateCSPs(messageCSPs)
        }

        return message
    }

    private fun messageToCharStylePair(message: Text): ArrayList<Pair<Char, Style>> {
        val messageCSPs = ArrayList<Pair<Char, Style>>()

        message.visit({ style: Style, content: String ->
            for (char in content) {
                messageCSPs.add(Pair(char, style))
            }
            Optional.empty<Any?>()
        }, Style.EMPTY)

        return messageCSPs
    }

    private fun applyPatternCSP(
        messageCSPs: MutableList<Pair<Char, Style>>,
        patternCSPs: Array<Pair<Char, Style>>
    ): Boolean {
        for (i in patternCSPs.indices) {
            val (msgChar, msgStyle) = messageCSPs[i]
            val (patChar, patStyle) = patternCSPs[i]

            if (msgChar != patChar) {
                logger.debug("Pattern char {} didn't match {}", msgChar, patChar)
                return false
            }

            messageCSPs[i] = msgChar to patStyle.withParent(msgStyle)
        }

        return true
    }

    private fun aggregateCSPs(messageCSPs: MutableList<Pair<Char, Style>>): Text {
        var groupId = 0
        var previousStyle = Style.EMPTY

        return messageCSPs.groupingBy { (_, style) ->
            if (style != previousStyle) {
                previousStyle = style
                ++groupId
            } else groupId
        }.aggregate { _, seg: Pair<StringBuilder, Style>?, (char, style), first ->
            if (first) StringBuilder().append(char) to style
            else {
                seg!!.first.append(char)
                seg
            }
        }.values.flatMap { (sb, style) -> Text.literal(sb.toString()).getWithStyle(style) }
            .fold(Text.empty()) { acc: MutableText, text: Text -> acc.append(text) }
    }
}
