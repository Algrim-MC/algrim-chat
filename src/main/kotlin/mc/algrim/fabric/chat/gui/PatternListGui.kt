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

    override fun shouldCloseOnEsc(): Boolean {
        return true
    }

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
        if (serverPatternStrings != null && serverPatternStrings.size > 0) {
            serverPatterns.addAll(serverPatternStrings.map {
                PatternWrapper(
                    it, PatternWrapper.Scope.SERVER, PatternWrapper.Type.PATTERN
                )
            })
            patterns.addAll(serverPatterns)
        }

        patterns.add(
            PatternWrapper(
                "<<<<<   ↑ Server / Global ↓   >>>>>", PatternWrapper.Scope.NONE, PatternWrapper.Type.SEPARATOR
            )
        )

        val globalPatternStrings = Config.globalConfig.patterns.strings
        if (globalPatternStrings.size > 0) {
            globalPatterns.addAll(Config.globalConfig.patterns.strings.map {
                PatternWrapper(
                    it, PatternWrapper.Scope.GLOBAL, PatternWrapper.Type.PATTERN
                )
            })
            patterns.addAll(globalPatterns)
        }
    }

    private fun resetLists() {
        patterns.clear()
        globalPatterns.clear()
        serverPatterns.clear()
    }

    private fun onTabChanged(tabId: ConfigTabWidget.TabId) {
        if (tabId != ConfigTabWidget.TabId.PATTERNS) {
            this.closeGui(true)
        } else {
            this.reCreateListWidget()
            this.getListWidget()?.resetScrollbarPosition()
            this.initGui()
        }
    }

    override fun onKeyTyped(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
//        if (keyCode == KeyCodes.KEY_ESCAPE) {
//            this.closeGui(false)
//            return true
//        }

        return super.onKeyTyped(keyCode, scanCode, modifiers)
    }

    private fun onButtonClicked(
        listItem: PatternListItemWidget, patternWrapper: PatternWrapper, buttonId: PatternListItemWidget.ButtonId
    ) {
        val entries = this.listWidget!!.currentEntries
        val patternIndexOffset =
            entries.indexOfFirst { it.scope == patternWrapper.scope && it.type == PatternWrapper.Type.PATTERN }
        val index = listItem.listIndex - patternIndexOffset

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

    private fun executeButtonFor(
        patterns: MutableList<PatternWrapper>, index: Int, buttonId: PatternListItemWidget.ButtonId
    ): Boolean {
        when (buttonId) {
            PatternListItemWidget.ButtonId.EDIT -> {
            }

            PatternListItemWidget.ButtonId.ADD -> {
            }

            PatternListItemWidget.ButtonId.MOVE_UP -> {
                if (index > 0) {
                    Collections.swap(patterns, index, index - 1)
                    return true
                }
            }

            PatternListItemWidget.ButtonId.MOVE_DOWN -> {
                if (index < patterns.size - 1) {
                    Collections.swap(patterns, index, index + 1)
                    return true
                }
            }

            PatternListItemWidget.ButtonId.REMOVE -> {
                patterns.removeAt(index)
                return true
            }
        }

        return false
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
            NONE, GLOBAL, SERVER
        }

        enum class Type {
            PATTERN, SEPARATOR,
        }
    }
}
