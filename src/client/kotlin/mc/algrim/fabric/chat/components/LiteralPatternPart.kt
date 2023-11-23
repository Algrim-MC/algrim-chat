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
        var partLength = 0

        for ((i, char) in remainder.toCharArray().withIndex()) {
            val escaped = if (i > 0) remainder[i - 1] == '\\' else false

            if (!escaped && untilChars.contains(char)) {
                partLength = i
                break
            } else if (i + 1 == remainder.length) {
                partLength = i + 1
            }

        }

        this.length = partLength
        this.value = remainder.substring(0, length)
    }

    override fun apply(messageContent: String, startIndex: Int): String? {
        if (messageContent.length < startIndex + value.length
            && !matches(messageContent.substring(startIndex, value.indices.last))
        ) return null
        return value
    }

    override fun toCharStylePair(messageContent: String, startIndex: Int): Array<Pair<Char, Style>>? {
        return apply(messageContent, startIndex)?.map { Pair(it, Style.EMPTY) }?.toTypedArray()
    }

    companion object : PatternPart.Parentheses {
        override val openChar = null
        override val closeChar = null
    }
}
