/*
 * This file is part of algrim-chat, a chat styling fabric mod.
 * Copyright (C) 2023.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mc.algrim.fabric.chat.gui

import fi.dy.masa.malilib.gui.GuiConfigsBase
import fi.dy.masa.malilib.gui.button.ButtonBase
import fi.dy.masa.malilib.gui.button.ButtonGeneric
import fi.dy.masa.malilib.gui.button.IButtonActionListener
import mc.algrim.fabric.chat.AlgrimChatClient.MOD_ID
import mc.algrim.fabric.chat.config.Config

class ConfigGui : GuiConfigsBase(10, 50, MOD_ID, null, "AlgrimChat Configs") {

    private var currentTab: Tab = Tab.GLOBAL

    private val stdMargin = 10

    override fun initGui() {
        super.initGui()
        initTabs()
    }

    private fun initTabs() {
        var x = stdMargin
        val hasServerConfig = Config.serverConfig != null

        for (tab in Tab.entries) {
            if (!hasServerConfig && tab == Tab.SERVER) {
                currentTab = Tab.GLOBAL
                continue
            }

            val buttonWidth = this.getStringWidth(tab.displayName) + stdMargin
            x += createTabButton(x, 22, buttonWidth, tab)
        }
    }

    private fun createTabButton(x: Int, y: Int, width: Int, tab: Tab): Int {
        val button = ButtonGeneric(x, y, width, 20, tab.displayName)
        button.setEnabled(currentTab != tab)
        addButton(button, TabButtonListener(tab, this))
        return button.width + 2
    }

    override fun onSettingsChanged() {
        super.onSettingsChanged()
        Config.reloadPatterns(Config.serverConfig!!)
    }

    override fun getConfigs(): MutableList<ConfigOptionWrapper> {
        return when (currentTab) {
            Tab.GLOBAL -> ConfigOptionWrapper.createFor(Config.globalConfig.chatOptions)
            Tab.SERVER -> ConfigOptionWrapper.createFor(Config.serverConfig!!.chatOptions)
        }
    }

    class TabButtonListener(private val tab: Tab, private val gui: ConfigGui) : IButtonActionListener {
        override fun actionPerformedWithButton(button: ButtonBase?, mouseButton: Int) {
            gui.currentTab = tab

            gui.reCreateListWidget()
            gui.getListWidget()?.resetScrollbarPosition()
            gui.initGui()
        }

    }

    enum class Tab(val displayName: String) {
        GLOBAL("Global Configs"),
        SERVER("Server Configs")
    }
}
