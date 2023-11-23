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

            if (i == remainder.length) {
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
        return apply(messageContent, startIndex)?.map { Pair(it, Style.EMPTY) }?.toTypedArray()
    }

    companion object : PatternPart.Parentheses {
        override val openChar = '/'
        override val closeChar = '/'
    }
}


//        val regexStringBuilder = StringBuilder()
//
//        for ((i, char) in chars.withIndex()) {
//            val isLiteral = if (i > 0) chars[i - 1] != '\\' else false
//
//            if (i == 0 && char != openChar)
//                throw IllegalArgumentException("String value '$messageContent', invalid open char, should be '${StyleTextPart.openChar}'")
//
//            if (!isLiteral) {
//                if (char == '\\') continue
//                else if (char == closeChar) break
//            }
//
//            regexStringBuilder.append(char)
//        }