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
        val stringLiteralBuilder = StringBuilder()

        for ((i, char) in remainder.toCharArray().withIndex()) {
            val escaped = if (i > 0) remainder[i - 1] == '\\' else false

            if (!escaped && char == '\\') continue

            if (!escaped && untilChars.contains(char)) {
                break
            }

            stringLiteralBuilder.append(char)
        }

        this.value = stringLiteralBuilder.toString()
        this.length = value.length
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
        return apply(messageContent, startIndex)?.map { Pair(it, Style.EMPTY) }?.toTypedArray()
    }

    companion object : PatternPart.Parentheses {
        override val openChar = null
        override val closeChar = null
    }
}
