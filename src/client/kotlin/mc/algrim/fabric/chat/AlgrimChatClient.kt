package mc.algrim.fabric.chat

import fi.dy.masa.malilib.event.InitializationHandler
import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory


object AlgrimChatClient : ClientModInitializer {

    const val MOD_ID = "algrim-chat"
    const val MOD_NAME = "AlgrimChat"

    //chat-sed

    @JvmStatic
    val logger = LoggerFactory.getLogger(MOD_ID)!!

    override fun onInitializeClient() {
        InitializationHandler.getInstance().registerInitializationHandler(InitHandler())
    }
}