/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
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

import fi.dy.masa.malilib.gui.GuiBase
import fi.dy.masa.malilib.gui.GuiConfigsBase
import mc.algrim.fabric.chat.AlgrimChat.MOD_ID
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.gui.widget.ConfigTabWidget

class ConfigGui : GuiConfigsBase(10, 50, MOD_ID, null, "AlgrimChat Configs") {
    override fun initGui() {
        super.initGui()
        addWidget(ConfigTabWidget(10, 22, ::onTabChanged))
    }

    fun onTabChanged(tabId: ConfigTabWidget.TabId) {
        if (tabId == ConfigTabWidget.TabId.PATTERNS) {
            GuiBase.openGui(PatternListGui())
        } else {
            this.reCreateListWidget()
            this.getListWidget()?.resetScrollbarPosition()
            this.initGui()
        }
    }

    override fun onSettingsChanged() {
        super.onSettingsChanged()
        Config.reloadPatterns(Config.serverConfig!!)
    }

    override fun getConfigs(): MutableList<ConfigOptionWrapper> {
        return when (ConfigTabWidget.currentTab) {
            ConfigTabWidget.TabId.GLOBAL -> ConfigOptionWrapper.createFor(Config.globalConfig.guiOptions)
            ConfigTabWidget.TabId.SERVER -> ConfigOptionWrapper.createFor(Config.serverConfig!!.guiOptions)
            else -> {
                GuiBase.openGui(PatternListGui()); return mutableListOf()
            }
        }
    }
}
