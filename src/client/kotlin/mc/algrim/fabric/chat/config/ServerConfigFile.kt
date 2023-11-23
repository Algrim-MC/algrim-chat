package mc.algrim.fabric.chat.config

import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.options.ConfigBoolean
import java.io.File

class ServerConfigFile(configFile: File) : ConfigFile(configFile) {

    val useGlobal = ConfigBoolean("useGlobal", true, "Whether global patterns apply on this server.")

    override val chatOptions = listOf<IConfigBase>(useGlobal, super.patterns)

    override val categories = mapOf("Chat" to chatOptions)

}