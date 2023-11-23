package mc.algrim.fabric.chat.components

import mc.algrim.fabric.chat.Utils
import net.minecraft.text.Style


class StylePatternPart(override val pattern: String, override val index: Int) : PatternPart {
    override val value: String
    override val length: Int
    val valuePattern: Pattern
    val style: String

    override fun matches(string: String): Boolean {
        return false
    }

    override fun apply(messageContent: String, startIndex: Int): String? {
        val forward = messageContent.substring(startIndex)
        return valuePattern.applyTransforms(forward)
    }

    override fun toCharStylePair(messageContent: String, startIndex: Int): Array<Pair<Char, Style>>? {
        return valuePattern.toCharStylePair(messageContent.substring(startIndex))
            ?.map { (char, style) -> Pair(char, style.withParent(Utils.styleFromString(this.style))) }
            ?.toTypedArray()
    }

    init {
        val remainder = pattern.substring(index)
        val styleStringBuilder = StringBuilder()
        val valueStringBuilder = StringBuilder()
        var finishedStyleIndex = -1
        var partLength = 0

        if (!remainder.startsWith(openChar)) {
            throw IllegalArgumentException("Pattern '$pattern', invalid open char, should be '$openChar'")
        }


        for ((i, char) in remainder.toCharArray()
            .withIndex()) { // TODO: Add an open char counting method to handle nested style parts.
            val escaped = if (i > 0) remainder[i - 1] == '\\' else false

            if (i < 1 || (!escaped && char == '\\')) continue

            if (finishedStyleIndex == -1 && !escaped && char == styleCloseChar) { // Style is finished if not escaped
                finishedStyleIndex = i
                continue
            }

            if (finishedStyleIndex == -1) {
                styleStringBuilder.append(char)
                continue
            }

            if (i == finishedStyleIndex + 1) { // Start of value declaration.
                if (char != valueOpenChar) throw IllegalArgumentException("Pattern '$pattern', is invalid. Closing style declaration ']', should be followed by '(' @$i")
                continue
            }

            if (!escaped && char == valueCloseChar) {
                partLength = i + 1 // idk why but meh
                break
            }

            if (i == remainder.length) {
                throw IllegalArgumentException("Pattern '$pattern', is invalid. Unexpected end of pattern.")
            }

            valueStringBuilder.append(char)
        }

        this.value = valueStringBuilder.toString()
        this.style = styleStringBuilder.toString()
        this.length = partLength
        this.valuePattern = Pattern.fromString(value)
    }


    companion object : PatternPart.Parentheses {
        val styleOpenChar = '['
        val styleCloseChar = ']'
        val valueOpenChar = '('
        val valueCloseChar = ')'
        override val openChar = styleOpenChar
        override val closeChar = valueCloseChar
    }
}
