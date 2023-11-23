package mc.algrim.fabric.chat.input

import fi.dy.masa.malilib.gui.GuiBase
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback
import fi.dy.masa.malilib.hotkeys.IKeybind
import fi.dy.masa.malilib.hotkeys.KeyAction
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.gui.ConfigGui

object KeyCallbacks : IHotkeyCallback {

    init {
        Config.globalConfig.configGuiHotKey.keybind.setCallback(this)
    }

    override fun onKeyAction(action: KeyAction, key: IKeybind): Boolean {
        return if (key == Config.globalConfig.configGuiHotKey.keybind) {
            GuiBase.openGui(ConfigGui())
            true
        } else false
    }
}