package mc.algrim.fabric.chat

import fi.dy.masa.malilib.config.ConfigManager
import fi.dy.masa.malilib.event.WorldLoadHandler
import fi.dy.masa.malilib.interfaces.IInitializationHandler
import mc.algrim.fabric.chat.AlgrimChatClient.MOD_ID
import mc.algrim.fabric.chat.config.Config


class InitHandler : IInitializationHandler {
    override fun registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, Config.globalConfig)

        WorldLoadHandler.getInstance().registerWorldLoadPreHandler(Config)
        WorldLoadHandler.getInstance().registerWorldLoadPostHandler(Config)
    }

}
