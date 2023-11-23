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
