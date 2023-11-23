package mc.algrim.fabric.chat.config

import com.google.common.collect.ImmutableList
import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.options.ConfigBoolean
import fi.dy.masa.malilib.config.options.ConfigStringList
import java.io.File

class ServerConfigFile(configFile: File) : ConfigFile(configFile) {

    val patterns = ConfigStringList("patterns", ImmutableList.of(), "Style patterns for chat on this server.")
    val useGlobal = ConfigBoolean("useGlobal", true, "Style patterns to be used on this server.")

    val chatOptions = listOf<IConfigBase>(useGlobal, patterns)

    override val categories: Map<String, List<IConfigBase>> = mapOf("Chat" to chatOptions)

    init {
        // Loading manually when joining server.
        this.load()
    }

}