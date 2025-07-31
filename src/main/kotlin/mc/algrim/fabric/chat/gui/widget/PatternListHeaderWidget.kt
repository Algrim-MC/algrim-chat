/*
 * This file is part of Algrim Chat, a chat styling fabric mod.
 * Copyright (C) 2024-2025.
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

import fi.dy.masa.malilib.gui.widgets.WidgetContainer
import fi.dy.masa.malilib.gui.widgets.WidgetLabel

class PatternListHeaderWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    nameColumnWidth: Int
) : WidgetContainer(x, y, width, height) {

    init {
        var labelX = x + 10
        val nameLabel = createLabel(
            labelX,
            y - 4 + this.height / 2,
            nameColumnWidth,
            (0xFFFFFFFF).toInt(),
            "Name"
        )
        labelX += nameLabel.width + 12

        createLabel(
            labelX,
            y - 4 + this.height / 2,
            this.width - nameColumnWidth - 50,
            (0xFFFFFFFF).toInt(),
            "Pattern"
        )
    }

    private fun createLabel(x: Int, y: Int, width: Int, colour: Int, text: String): WidgetLabel {
        val textWidth = this.getStringWidth(text)
        val labelValue = if (textWidth > width) {
            this.textRenderer.trimToWidth(text, width - 10) + "…"
        } else text

        val label = WidgetLabel(x, y, width, 10, colour, labelValue)

        addWidget(label)

        return label
    }
}
