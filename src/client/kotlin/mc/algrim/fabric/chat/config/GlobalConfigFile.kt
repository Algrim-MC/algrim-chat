package mc.algrim.fabric.chat.config

import com.google.common.collect.ImmutableList
import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.options.ConfigBoolean
import fi.dy.masa.malilib.config.options.ConfigHotkey
import fi.dy.masa.malilib.config.options.ConfigStringList
import java.io.File

class GlobalConfigFile(configFile: File) : ConfigFile(configFile) {

    val enabled = ConfigBoolean("enabled", true, "Enable chat styling")
    val patterns = ConfigStringList("patterns", ImmutableList.of(), "Style patterns to be used globally")
    var configGuiHotKey = ConfigHotkey("openConfigGui", "Y,C", "The hotkey used to open the config gui")

    val chatOptions = listOf<IConfigBase>(enabled, configGuiHotKey, patterns)

    override val categories = mapOf("Chat" to chatOptions)

}