package mc.algrim.fabric.chat.gui.widget

import fi.dy.masa.malilib.gui.widgets.WidgetListBase
import mc.algrim.fabric.chat.gui.PatternListGui

class PatternListWidget(
    x: Int,
    y: Int,
    val widgetWidth: Int,
    widgetHeight: Int,
    val parentGui: PatternListGui,
    val eventHandler: ((listItem: PatternListItemWidget, patternWrapper: PatternListGui.PatternWrapper, buttonId: PatternListItemWidget.ButtonId) -> Unit)? = null
) : WidgetListBase<PatternListGui.PatternWrapper, PatternListItemWidget>(x, y, widgetWidth, widgetHeight, {}) {
    override fun createListEntryWidget(
        x: Int, y: Int, listIndex: Int, isOdd: Boolean, entry: PatternListGui.PatternWrapper
    ): PatternListItemWidget {
        return PatternListItemWidget(x, y, this.widgetWidth, entry, listIndex, eventHandler)
    }

    override fun getAllEntries(): Collection<PatternListGui.PatternWrapper> {
        return parentGui.patterns
    }
}
