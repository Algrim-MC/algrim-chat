package mc.algrim.fabric.chat.components

import kotlin.test.Test

class PatternTest {
    @Test
    fun test() {
        val pattern = Pattern.fromString("This is\\[ a [green](test) /pa(t)+ern/ things")
        val vanillaPattern = Pattern.fromString("<[#0000FF](/[0-Z]+/)> [#FF0000](/.*/)")
        val transformed = vanillaPattern.applyTransforms("<Sainty97> penises")
        println(pattern.parts.joinToString("|") { it.value })
    }
}