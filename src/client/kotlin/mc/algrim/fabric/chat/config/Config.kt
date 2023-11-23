package mc.algrim.fabric.chat.config

import fi.dy.masa.malilib.gui.Message
import fi.dy.masa.malilib.interfaces.IWorldLoadListener
import fi.dy.masa.malilib.util.FileUtils
import fi.dy.masa.malilib.util.InfoUtils
import fi.dy.masa.malilib.util.StringUtils
import mc.algrim.fabric.chat.AlgrimChatClient
import mc.algrim.fabric.chat.AlgrimChatClient.MOD_ID
import mc.algrim.fabric.chat.ChatProcessor
import mc.algrim.fabric.chat.components.Pattern
import net.minecraft.client.MinecraftClient
import net.minecraft.client.world.ClientWorld
import java.io.File

object Config : IWorldLoadListener {
    val globalConfig = GlobalConfigFile(getGlobalConfigFile())
    var serverConfig: ServerConfigFile? = null

    fun reloadPatterns(serverConfig: ServerConfigFile) {
        val effectivePatternStrings = ArrayList<String>()

        effectivePatternStrings.addAll(serverConfig.patterns.strings)

        if (serverConfig.useGlobal.booleanValue) {
            effectivePatternStrings.addAll(globalConfig.patterns.strings)
        }

        val patterns = effectivePatternStrings.mapNotNull {
            try {
                Pattern.fromString(it)
            } catch (e: IllegalArgumentException) {
                InfoUtils.showGuiOrActionBarMessage(
                    Message.MessageType.ERROR,
                    8000,
                    "Error initializing pattern \"$it\"."
                )
                AlgrimChatClient.logger.error("Error initializing pattern!", e)
                null
            }
        }

        ChatProcessor.enabled = globalConfig.enabled.booleanValue
        ChatProcessor.patterns.clear()
        ChatProcessor.patterns.addAll(patterns)
    }

    private fun getGlobalConfigFile(): File {
        return File(FileUtils.getConfigDirectory(), "$MOD_ID.json")
    }

    private fun getServerConfigFile(): File {
        val configDir = File(FileUtils.getConfigDirectory(), MOD_ID)

        if (!configDir.exists() && !configDir.mkdirs()) {
            AlgrimChatClient.logger.error("Unable to create config directory! '${configDir.path}'")
        }

        return File(configDir, StringUtils.getStorageFileName(true, "", ".json", "_unknown"))
    }

    override fun onWorldLoadPre(worldBefore: ClientWorld?, worldAfter: ClientWorld?, mc: MinecraftClient) {
        if (worldBefore != null && worldAfter == null) {
            serverConfig?.save()
        }
    }

    override fun onWorldLoadPost(worldBefore: ClientWorld?, worldAfter: ClientWorld?, mc: MinecraftClient) {
        if (worldBefore == null && worldAfter != null) {
            val newServerConfig = ServerConfigFile(getServerConfigFile())
            serverConfig = newServerConfig

            reloadPatterns(newServerConfig)
        }
    }
}