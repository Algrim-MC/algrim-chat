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
import fi.dy.masa.malilib.gui.GuiListBase
import mc.algrim.fabric.chat.AlgrimChat
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.gui.widget.ConfigTabWidget
import mc.algrim.fabric.chat.gui.widget.PatternListItemWidget
import mc.algrim.fabric.chat.gui.widget.PatternListWidget
import java.util.*

class PatternListGui : GuiListBase<PatternListGui.PatternWrapper, PatternListItemWidget, PatternListWidget>(10, 40) {
    val patterns = mutableListOf<PatternWrapper>()
    val globalPatterns = mutableListOf<PatternWrapper>()
    val serverPatterns = mutableListOf<PatternWrapper>()

    init {
        this.setTitle("Patterns")
    }

    override fun initGui() {
        initLists()
        super.initGui()
        addWidget(ConfigTabWidget(10, 22, ::onTabChanged))
    }

    private fun initLists() {
        resetLists()
        val serverPatternStrings = Config.serverConfig?.patterns?.strings
        val globalPatternStrings = Config.globalConfig.patterns.strings

        if (serverPatternStrings != null) {
            if (serverPatternStrings.size > 0) {
                serverPatterns.addAll(serverPatternStrings.map {
                    PatternWrapper(
                        it, PatternWrapper.Scope.SERVER, PatternWrapper.Type.PATTERN
                    )
                })
                patterns.addAll(serverPatterns)
            } else {
                patterns.add(PatternWrapper("", PatternWrapper.Scope.SERVER, PatternWrapper.Type.EMPTY_SCOPE))
            }
        }

        patterns.add(
            PatternWrapper(
                "<<<<<   ↑ Server / Global ↓   >>>>>", PatternWrapper.Scope.NONE, PatternWrapper.Type.SEPARATOR
            )
        )

        if (globalPatternStrings.size > 0) {
            globalPatterns.addAll(Config.globalConfig.patterns.strings.map {
                PatternWrapper(
                    it, PatternWrapper.Scope.GLOBAL, PatternWrapper.Type.PATTERN
                )
            })
            patterns.addAll(globalPatterns)
        } else {
            patterns.add(PatternWrapper("", PatternWrapper.Scope.GLOBAL, PatternWrapper.Type.EMPTY_SCOPE))
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
        listItem: PatternListItemWidget, patternWrapper: PatternWrapper, buttonId: PatternListItemWidget.ButtonId
    ) {
        val entries = this.listWidget!!.currentEntries
        val patternIndexOffset =
            entries.indexOfFirst { it.scope == patternWrapper.scope && it.type == PatternWrapper.Type.PATTERN }
        val index =
            if (patternWrapper.type == PatternWrapper.Type.EMPTY_SCOPE) 0 else listItem.listIndex - patternIndexOffset

        AlgrimChat.logger.info("Got event ${patternWrapper.value} $buttonId")

        val modified = when (buttonId) {
            PatternListItemWidget.ButtonId.EDIT -> {
                val newPatternGui = NewPatternGui(patternWrapper.value, patternWrapper.scope, index, true)
                newPatternGui.setParent(this)

                GuiBase.openGui(newPatternGui)
                false
            }

            PatternListItemWidget.ButtonId.ADD -> {
                val newPatternGui = NewPatternGui("", patternWrapper.scope, index)
                newPatternGui.setParent(this)

                GuiBase.openGui(newPatternGui)
                false
            }

            else -> when (patternWrapper.scope) {
                PatternWrapper.Scope.SERVER -> {
                    when (buttonId) {
                        PatternListItemWidget.ButtonId.MOVE_UP -> {
                            if (index > 0) {
                                Collections.swap(serverPatterns, index, index - 1)
                                true
                            } else false
                        }

                        PatternListItemWidget.ButtonId.MOVE_DOWN -> {
                            if (index < serverPatterns.size - 1) {
                                Collections.swap(serverPatterns, index, index + 1)
                                true
                            } else if (index == serverPatterns.size - 1) {
                                globalPatterns.add(0, serverPatterns.removeAt(index))
                                true
                            } else false
                        }

                        PatternListItemWidget.ButtonId.REMOVE -> {
                            serverPatterns.removeAt(index)
                            true
                        }

                        else -> false
                    }
                }

                PatternWrapper.Scope.GLOBAL -> {
                    when (buttonId) {
                        PatternListItemWidget.ButtonId.MOVE_UP -> {
                            if (index > 0) {
                                Collections.swap(globalPatterns, index, index - 1)
                                true
                            } else if (index == 0) {
                                serverPatterns.add(globalPatterns.removeAt(index))
                                true
                            } else false
                        }

                        PatternListItemWidget.ButtonId.MOVE_DOWN -> {
                            if (index < globalPatterns.size - 1) {
                                Collections.swap(globalPatterns, index, index + 1)
                                true
                            } else false
                        }

                        PatternListItemWidget.ButtonId.REMOVE -> {
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
        Config.globalConfig.patterns.strings = globalPatterns.map { it.value }
        Config.serverConfig!!.patterns.strings = serverPatterns.map { it.value }
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

    class PatternWrapper(val value: String, val scope: Scope, val type: Type) {

        enum class Scope {
            NONE,
            GLOBAL,
            SERVER
        }

        enum class Type {
            PATTERN,
            SEPARATOR,
            EMPTY_SCOPE
        }
    }
}
