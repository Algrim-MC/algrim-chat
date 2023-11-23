package mc.algrim.fabric.chat

import mc.algrim.fabric.chat.AlgrimChatClient.logger
import mc.algrim.fabric.chat.components.Pattern
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
//    val pattern =
//        Pattern.fromString("test")


//    val pattern =
//        Pattern.fromString("\\[[#821212](/\\(Mod |God |Goddess |Demigod |Archangel |Angel |Mortal \\)?/)[#00FFFF](/\\\\w+/)[#909090](/[^→]*/)→ [white,!i,!b](/.*/)")

    val pattern =
        Pattern.fromString("\\[-] [red](/.*/)")

//    val charStylePairs = pattern.toCharStylePair("testp")
//    val charStylePairs = pattern.toCharStylePair("Player Title 30 → Stuff Player says")
//    val charStylePairs = pattern.toCharStylePair("God Player Title 30 → Stuff Player says")
//    val charStylePairs = pattern.toCharStylePair("[Local] Player → Stuff Player says")
//    val charStylePairs = pattern.toCharStylePair("[Sainty A Title 25 → samee")
    val charStylePairs = pattern.toCharStylePair("[-] Player left the game")

    pattern.parts.forEach { println("${it::class.simpleName} ${it.value}") }

    println(measureTimeMillis {
        charStylePairs?.forEach { (char, style) ->
            println("$char $style")
        } ?: println("No match")
    })
}

object ChatProcessor {
    val patterns = ArrayList<Pattern>()

    fun execute(message: Text): Text {
        val messageContent = message.string
        val messageCSPs = ArrayList<Pair<Char, Style>>()
        message.visit({ style: Style, content: String ->
            for (char in content) {
                messageCSPs.add(Pair(char, style))
            }
            Optional.empty<Any?>()
        }, Style.EMPTY)


        patterns.forEach { pattern ->
            val patternCSPs = pattern.toCharStylePair(messageContent)

            if (patternCSPs == null) {
                logger.info("Pattern '$pattern' didn't match '$messageContent'")
                return@forEach
            }

            if (patternCSPs.size != messageCSPs.size) {
                logger.info("Pattern '$pattern'${patternCSPs.size} didn't match '$messageContent'${messageCSPs.size}")
                return@forEach
            }

            for (i in patternCSPs.indices) {
                val (msgChar, msgStyle) = messageCSPs[i]
                val (patChar, patStyle) = patternCSPs[i]

                logger.debug("{} {} {}", msgChar, msgStyle, patStyle)

                if (msgChar != patChar) {
                    logger.info("Pattern '$pattern'$msgChar didn't match '$messageContent'$patChar")
                    return@forEach
                }

                messageCSPs[i] = Pair(msgChar, patStyle.withParent(msgStyle))
            }


            var groupId = 0
            var previousStyle = Style.EMPTY

            val styledText = messageCSPs.groupingBy { (_, style) ->
                if (style != previousStyle) {
                    previousStyle = style
                    ++groupId
                } else groupId
            }.aggregate { _, seg: Pair<StringBuilder, Style>?, (char, style), first ->
                if (first) Pair(StringBuilder().append(char), style)
                else {
                    seg!!.first.append(char)
                    seg
                }
            }.values.flatMap { (sb, style) -> Text.literal(sb.toString()).getWithStyle(style) }
                .fold(Text.empty()) { acc: MutableText, text: Text -> acc.append(text) }

            logger.info(styledText.string)

            return styledText
        }

        return message
    }

}
