/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2023-2024.
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

import fi.dy.masa.malilib.gui.widgets.WidgetListBase
import mc.algrim.fabric.chat.gui.PatternListGui
import mc.algrim.fabric.chat.gui.data.ListItemData

class PatternListWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val parentGui: PatternListGui,
    val eventHandler: ((listItem: PatternListItemWidget, listItemData: ListItemData, buttonId: PatternListItemWidget.ButtonId) -> Unit)? = null
) : WidgetListBase<ListItemData, PatternListItemWidget>(x, y, width, height, {}) {
    override fun createListEntryWidget(
        x: Int, y: Int, listIndex: Int, isOdd: Boolean, entry: ListItemData
    ): PatternListItemWidget {
        return PatternListItemWidget(
            x,
            y,
            this.browserEntryWidth,
            entry,
            listIndex,
            parentGui.nameColumnWidth,
            eventHandler
        )
    }

    override fun getAllEntries(): Collection<ListItemData> {
        return parentGui.patterns
    }
}
