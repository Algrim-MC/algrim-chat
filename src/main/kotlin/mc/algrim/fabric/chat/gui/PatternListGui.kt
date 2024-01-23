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

package mc.algrim.fabric.chat.gui

import fi.dy.masa.malilib.gui.GuiBase
import fi.dy.masa.malilib.gui.GuiListBase
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.gui.data.EmptyPatternListEntry
import mc.algrim.fabric.chat.gui.data.PatternListEntry
import mc.algrim.fabric.chat.gui.data.PatternPatternListEntry
import mc.algrim.fabric.chat.gui.data.SeparatorPatternListEntry
import mc.algrim.fabric.chat.gui.widget.ConfigTabWidget
import mc.algrim.fabric.chat.gui.widget.PatternListEntryWidget
import mc.algrim.fabric.chat.gui.widget.PatternListHeaderWidget
import mc.algrim.fabric.chat.gui.widget.PatternListWidget
import java.util.*

class PatternListGui : GuiListBase<PatternListEntry, PatternListEntryWidget, PatternListWidget>(10, 64) {
    val patterns = mutableListOf<PatternListEntry>()
    val globalPatterns = mutableListOf<PatternPatternListEntry>()
    val serverPatterns = mutableListOf<PatternPatternListEntry>()

    val nameColumnWidth: Int
        get() = (((this.browserWidth - 20) * 0.15).toInt())

    init {
        this.setTitle("Patterns")
    }

    override fun initGui() {
        initLists()
        super.initGui()
        addWidget(ConfigTabWidget(10, 22, ::onTabChanged))
        addWidget(PatternListHeaderWidget(10, 46, this.browserWidth, 20, this.nameColumnWidth))
    }

    private fun initLists() {
        resetLists()
        val serverPatternValues = Config.serverConfig?.patterns?.patternOptionValues
        val globalPatternValues = Config.globalConfig.patterns.patternOptionValues

        if (serverPatternValues != null) {
            if (serverPatternValues.isNotEmpty()) {
                serverPatterns.addAll(serverPatternValues.map {
                    PatternPatternListEntry(
                        it,
                        PatternListEntry.Scope.SERVER
                    )
                })
                patterns.addAll(serverPatterns)
            } else {
                patterns.add(EmptyPatternListEntry(PatternListEntry.Scope.SERVER))
            }
        }

        patterns.add(SeparatorPatternListEntry("<<<<<   ↑ Server / Global ↓   >>>>>"))

        if (globalPatternValues.isNotEmpty()) {
            globalPatterns.addAll(globalPatternValues.map {
                PatternPatternListEntry(
                    it,
                    PatternListEntry.Scope.GLOBAL
                )
            })
            patterns.addAll(globalPatterns)
        } else {
            patterns.add(EmptyPatternListEntry(PatternListEntry.Scope.GLOBAL))
        }
    }

    private fun resetLists() {
        patterns.clear()
        globalPatterns.clear()
        serverPatterns.clear()
    }

    private fun onTabChanged(tabId: ConfigTabWidget.TabId) {
        if (tabId != ConfigTabWidget.TabId.PATTERNS) {
            GuiBase.openGui(ConfigGui())
        } else {
            this.reCreateListWidget()
            this.getListWidget()?.resetScrollbarPosition()
            this.initGui()
        }
    }

    private fun onButtonClicked(
        listItem: PatternListEntryWidget, itemData: PatternListEntry, buttonId: PatternListEntryWidget.ButtonId
    ) {
        val entries = this.listWidget!!.currentEntries
        val patternIndexOffset =
            entries.indexOfFirst { it.scope == itemData.scope && it.type == PatternListEntry.Type.PATTERN }
        val index =
            if (itemData.type == PatternListEntry.Type.EMPTY_SCOPE) 0 else listItem.listIndex - patternIndexOffset

        val modified = when (buttonId) {
            PatternListEntryWidget.ButtonId.EDIT -> {
                if (itemData !is PatternPatternListEntry) return
                val newPatternGui =
                    NewPatternGui(itemData.value.name, itemData.value.patternValue, itemData.scope, index, true)
                newPatternGui.setParent(this)

                GuiBase.openGui(newPatternGui)
                false
            }

            PatternListEntryWidget.ButtonId.ADD -> {
                val newPatternGui = NewPatternGui(scope = itemData.scope, patternIndex = index)
                newPatternGui.setParent(this)

                GuiBase.openGui(newPatternGui)
                false
            }

            else -> when (itemData.scope) {
                PatternListEntry.Scope.SERVER -> {
                    when (buttonId) {
                        PatternListEntryWidget.ButtonId.ENABLE,
                        PatternListEntryWidget.ButtonId.DISABLE -> {
                            if (itemData is PatternPatternListEntry) {
                                serverPatterns[index] = PatternPatternListEntry(
                                    itemData.value.withEnabled(buttonId == PatternListEntryWidget.ButtonId.ENABLE),
                                    PatternListEntry.Scope.SERVER
                                )
                                true
                            } else false
                        }

                        PatternListEntryWidget.ButtonId.MOVE_UP -> {
                            if (index > 0) {
                                Collections.swap(serverPatterns, index, index - 1)
                                true
                            } else false
                        }

                        PatternListEntryWidget.ButtonId.MOVE_DOWN -> {
                            if (index < serverPatterns.size - 1) {
                                Collections.swap(serverPatterns, index, index + 1)
                                true
                            } else if (index == serverPatterns.size - 1) {
                                globalPatterns.add(0, serverPatterns.removeAt(index))
                                true
                            } else false
                        }

                        PatternListEntryWidget.ButtonId.REMOVE -> {
                            serverPatterns.removeAt(index)
                            true
                        }

                        else -> false
                    }
                }

                PatternListEntry.Scope.GLOBAL -> {
                    when (buttonId) {
                        PatternListEntryWidget.ButtonId.ENABLE,
                        PatternListEntryWidget.ButtonId.DISABLE -> {
                            if (itemData is PatternPatternListEntry) {
                                globalPatterns[index] = PatternPatternListEntry(
                                    itemData.value.withEnabled(buttonId == PatternListEntryWidget.ButtonId.ENABLE),
                                    PatternListEntry.Scope.GLOBAL
                                )
                                true
                            } else false
                        }

                        PatternListEntryWidget.ButtonId.MOVE_UP -> {
                            if (index > 0) {
                                Collections.swap(globalPatterns, index, index - 1)
                                true
                            } else if (index == 0) {
                                serverPatterns.add(globalPatterns.removeAt(index))
                                true
                            } else false
                        }

                        PatternListEntryWidget.ButtonId.MOVE_DOWN -> {
                            if (index < globalPatterns.size - 1) {
                                Collections.swap(globalPatterns, index, index + 1)
                                true
                            } else false
                        }

                        PatternListEntryWidget.ButtonId.REMOVE -> {
                            globalPatterns.removeAt(index)
                            true
                        }

                        else -> false
                    }
                }

                else -> false

            }
        }

        if (modified) {
            updateConfig()
            initGui()
        }
    }

    private fun updateConfig() {
        Config.globalConfig.patterns.patternOptionValues = globalPatterns.map { it.value }
        Config.serverConfig!!.patterns.patternOptionValues = serverPatterns.map { it.value }
        Config.reloadPatterns(Config.serverConfig!!)
    }

    override fun createListWidget(listX: Int, listY: Int): PatternListWidget {
        return PatternListWidget(listX, listY, browserWidth, browserHeight, this, ::onButtonClicked)
    }

    override fun getBrowserWidth(): Int {
        return this.width - 20
    }

    override fun getBrowserHeight(): Int {
        return this.height - 80
    }
}
