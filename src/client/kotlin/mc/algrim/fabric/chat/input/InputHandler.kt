package mc.algrim.fabric.chat.input

import fi.dy.masa.malilib.hotkeys.IKeybindManager
import fi.dy.masa.malilib.hotkeys.IKeybindProvider
import mc.algrim.fabric.chat.AlgrimChatClient.MOD_NAME
import mc.algrim.fabric.chat.config.Config

object InputHandler : IKeybindProvider {
    override fun addKeysToMap(manager: IKeybindManager) {
        manager.addKeybindToMap(Config.globalConfig.configGuiHotKey.keybind)
    }

    override fun addHotkeys(manager: IKeybindManager) {
        manager.addHotkeysForCategory(MOD_NAME, "Chat", listOf(Config.globalConfig.configGuiHotKey))
    }
}