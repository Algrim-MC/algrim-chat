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
import mc.algrim.fabric.chat.gui.data.*
import mc.algrim.fabric.chat.gui.widget.ConfigTabWidget
import mc.algrim.fabric.chat.gui.widget.PatternListEntryWidget
import mc.algrim.fabric.chat.gui.widget.PatternListHeaderWidget
import mc.algrim.fabric.chat.gui.widget.PatternListWidget
import java.util.*

class PatternListGui : GuiListBase<PatternListEntryData, PatternListEntryWidget, PatternListWidget>(10, 64) {
    val patterns = mutableListOf<PatternListEntryData>()
    val globalPatterns = mutableListOf<PatternEntryData>()
    val serverPatterns = mutableListOf<PatternEntryData>()

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
                serverPatterns.addAll(serverPatternValues.mapIndexed { i, it ->
                    PatternEntryData(it, PatternScope.SERVER, i)
                })

                patterns.addAll(serverPatterns)
            } else {
                patterns.add(EmptyEntryData(PatternScope.SERVER))
            }
        }

        patterns.add(SeparatorEntryData("<<<<<   ↑ Server / Global ↓   >>>>>"))

        if (globalPatternValues.isNotEmpty()) {
            globalPatterns.addAll(globalPatternValues.mapIndexed { i, it ->
                PatternEntryData(it, PatternScope.GLOBAL, i)
            })
            patterns.addAll(globalPatterns)
        } else {
            patterns.add(EmptyEntryData(PatternScope.GLOBAL))
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

    // This whole thing is a mess, but I've spent way too much time thinking of a better way of doing this. :(
    private fun onButtonClicked(
        listItem: PatternListEntryWidget, entryData: PatternListEntryData, buttonId: PatternListEntryWidget.ButtonId
    ) {

        val (index, relativePatterns) =
            if (entryData is PatternEntryData) {
                entryData.index to when (entryData.scope) {
                    PatternScope.GLOBAL -> globalPatterns
                    PatternScope.SERVER -> serverPatterns
                }
            } else 0 to null

        val modified = if (buttonId == PatternListEntryWidget.ButtonId.ADD && entryData is ScopedEntryData) {
            val newPatternGui = NewPatternGui(scope = entryData.scope, patternIndex = index)

            newPatternGui.setParent(this)
            openGui(newPatternGui)
            false
        } else if (entryData is PatternEntryData) when (buttonId) {
            PatternListEntryWidget.ButtonId.ENABLE,
            PatternListEntryWidget.ButtonId.DISABLE -> {
                relativePatterns!![index] = PatternEntryData(
                    entryData.value.withEnabled(buttonId == PatternListEntryWidget.ButtonId.ENABLE),
                    entryData.scope,
                    index
                )
                true
            }

            PatternListEntryWidget.ButtonId.EDIT -> {
                val newPatternGui =
                    NewPatternGui(entryData.value.name, entryData.value.patternValue, entryData.scope, index, true)

                newPatternGui.setParent(this)
                openGui(newPatternGui)
                false
            }

            PatternListEntryWidget.ButtonId.REMOVE -> {
                relativePatterns!!.removeAt(index)
                true
            }

            else -> when (entryData.scope) {
                PatternScope.SERVER -> {
                    when (buttonId) {

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

                        else -> false
                    }
                }

                PatternScope.GLOBAL -> {
                    when (buttonId) {

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

                        else -> false
                    }
                }
            }
        } else false

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
