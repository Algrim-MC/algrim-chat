package mc.algrim.fabric.chat.gui.widget

import fi.dy.masa.malilib.gui.widgets.WidgetListBase
import mc.algrim.fabric.chat.gui.PatternListGui

class PatternListWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    val parentGui: PatternListGui,
    val eventHandler: ((listItem: PatternListItemWidget, patternWrapper: PatternListGui.PatternWrapper, buttonId: PatternListItemWidget.ButtonId) -> Unit)? = null
) : WidgetListBase<PatternListGui.PatternWrapper, PatternListItemWidget>(x, y, width, height, {}) {
    override fun createListEntryWidget(
        x: Int, y: Int, listIndex: Int, isOdd: Boolean, entry: PatternListGui.PatternWrapper
    ): PatternListItemWidget { // TODO: This results in the width never changing after initialization
        return PatternListItemWidget(x, y, this.browserEntryWidth, entry, listIndex, eventHandler)
    }

    override fun getAllEntries(): Collection<PatternListGui.PatternWrapper> {
        return parentGui.patterns
    }
}
