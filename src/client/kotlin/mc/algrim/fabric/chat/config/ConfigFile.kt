package mc.algrim.fabric.chat.config

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fi.dy.masa.malilib.config.ConfigUtils
import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.IConfigHandler
import fi.dy.masa.malilib.util.JsonUtils
import java.io.File

abstract class ConfigFile(val configFile: File) : IConfigHandler {

    abstract val categories: Map<String, List<IConfigBase>>

    override fun load() {
        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            val element: JsonElement? = JsonUtils.parseJsonFile(configFile)
            if (element != null && element.isJsonObject) {
                val root: JsonObject = element.asJsonObject

                for ((category, options) in categories) {
                    ConfigUtils.readConfigBase(root, category, options)
                }
            }
        }
    }

    override fun save() {
        if (configFile.parentFile.exists() && configFile.parentFile.isDirectory() || configFile.parentFile.mkdirs()) {
            val root = JsonObject()

            for ((category, options) in categories) {
                ConfigUtils.writeConfigBase(root, category, options)
            }

            JsonUtils.writeJsonToFile(root, configFile)
        }
    }

}