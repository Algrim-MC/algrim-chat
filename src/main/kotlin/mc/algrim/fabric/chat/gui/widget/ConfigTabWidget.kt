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

package mc.algrim.fabric.chat.gui.widget

import fi.dy.masa.malilib.gui.button.ButtonBase
import fi.dy.masa.malilib.gui.button.ButtonGeneric
import fi.dy.masa.malilib.gui.widgets.WidgetContainer
import mc.algrim.fabric.chat.config.Config

class ConfigTabWidget(x: Int, y: Int, private val tabChangedListener: ((tabId: TabId) -> Unit)? = null) :
    WidgetContainer(x, y, -1, 20) {
    init {
        var buttonX = x
        val hasServerConfig = Config.serverConfig != null

        for (tabId in TabId.entries) {
            if (!hasServerConfig && tabId == TabId.SERVER) {
                currentTab = TabId.GLOBAL
                continue
            }

            val button = createTabButton(buttonX, tabId)
            buttonX += button.width + 2
        }

        this.width = buttonX - x
    }

    private fun createTabButton(x: Int, tabId: TabId): ButtonGeneric {
        val width = this.getStringWidth(tabId.displayName) + 10
        val button = ButtonGeneric(x, this.y, width, 20, tabId.displayName)

        button.setEnabled(currentTab != tabId)
        this.addButton(button) { _: ButtonBase, _: Int -> currentTab = tabId; tabChangedListener?.invoke(tabId) }

        return button
    }

    enum class TabId(val displayName: String) {
        GLOBAL("Global Configs"),
        SERVER("Server Configs"),
        PATTERNS("Patterns")
    }

    companion object {
        var currentTab: TabId = TabId.GLOBAL
    }
}
