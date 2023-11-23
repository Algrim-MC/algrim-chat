import mc.algrim.fabric.chat.components.Pattern
import kotlin.system.measureTimeMillis
import kotlin.test.Test

class ChatProcessorTest {
    @Test
    fun test() {
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

        println(
            "in ${
                measureTimeMillis {
                    charStylePairs?.forEach { (char, style) ->
                        println("$char $style")
                    } ?: println("No match")
                }
            }ms"
        )
    }
}