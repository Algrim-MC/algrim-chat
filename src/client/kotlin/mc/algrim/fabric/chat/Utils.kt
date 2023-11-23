package mc.algrim.fabric.chat

import net.minecraft.text.TextColor
import net.minecraft.text.Style as MinecraftStyle

object Utils {
    fun styleFromString(string: String): MinecraftStyle {
        return styleFromString(string, MinecraftStyle.EMPTY)
    }

    fun styleFromString(string: String, defaultStyle: MinecraftStyle): MinecraftStyle {
        var style = defaultStyle
        val args = string.split(",").map { it.trim().lowercase() }

        for (arg in args) {
            val opt = !arg.startsWith("!")

            style = when (if (opt) arg else arg.substring(1)) {
                "bold", "b" -> style.withBold(opt)
                "italic", "it", "i" -> style.withItalic(opt)
                "underlined", "u" -> style.withUnderline(opt)
                "strikethrough", "s" -> style.withStrikethrough(opt)
                "obfuscated", "o" -> style.withObfuscated(opt)
                else -> style.withColor(TextColor.parse(arg))
            }
        }

        return style
    }
}