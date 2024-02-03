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

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric
import fi.dy.masa.malilib.gui.Message
import fi.dy.masa.malilib.gui.button.ButtonGeneric
import fi.dy.masa.malilib.gui.button.IButtonActionListener
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener
import fi.dy.masa.malilib.gui.widgets.WidgetLabel
import fi.dy.masa.malilib.util.InfoUtils
import mc.algrim.fabric.chat.ChatProcessor
import mc.algrim.fabric.chat.components.Pattern
import mc.algrim.fabric.chat.config.Config
import mc.algrim.fabric.chat.config.option.PatternOption
import mc.algrim.fabric.chat.gui.data.PatternScope
import mc.algrim.fabric.chat.gui.widget.GuiTextField
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class NewPatternGui(
    private var name: String = "",
    private var patternStr: String = "",
    private val scope: PatternScope,
    private val patternIndex: Int,
    private val replace: Boolean = false
) : AlgrimGuiBase() {

    private var testMsgStr = ""

    private var resultText: Text = ScreenTexts.EMPTY
        set(value) {
            field = value
            initGui()
        }

    private val stdMargin = 10
    private val stdWidgetHeight = 20

    override fun initGui() {
        super.initGui()

        var y = stdWidgetHeight

        val nameLabel = createTextLabel(stdMargin, y, "Name")
        y += nameLabel.height + 2
        val nameTextField = createTextField(stdMargin, y, this.width - stdMargin * 2, name) { f ->
            name = f.text; return@createTextField true
        }
        y += nameTextField.height + stdMargin

        val patternLabel = createTextLabel(stdMargin, y, "Pattern")
        y += patternLabel.height + 2

        val patternTextField = createTextField(stdMargin, y, this.width - stdMargin * 2, patternStr) { f ->
            patternStr = f.text; return@createTextField true
        }
        y += patternTextField.height + stdMargin

        val testLabel = createTextLabel(stdMargin, y, "Test Message")
        y += testLabel.height + 2

        val testButton = createButton(this.width - stdMargin, y, "Test") { _, _ -> testPattern() }
        val testTextField = createTextField(
            stdMargin,
            y,
            this.width - testButton.width - 2 - stdMargin * 2,
            testMsgStr
        ) { f -> testMsgStr = f.text; return@createTextField true }
        testButton.x -= testButton.width
        y += testTextField.height + stdMargin


        val resultLabel = createTextLabel(stdMargin, y, "Result: ")
        val resultTextWidget = createTextWidget(resultLabel.width + stdMargin, y, resultText)
        y += resultTextWidget.height + stdMargin


        val saveButton = createButton(this.width - stdMargin, y, "Save") { _, _ -> save() }
        val cancelButton = createButton(this.width - stdMargin, y, "Cancel") { _, _ -> cancel() }
        saveButton.x -= cancelButton.width + saveButton.width + 2
        cancelButton.x -= cancelButton.width
    }

    private fun showMessage(message: String) {
        InfoUtils.showGuiOrActionBarMessage(
            Message.MessageType.ERROR,
            4000,
            message
        )
    }

    private fun save() {
        try {
            if (patternStr == "") {
                showMessage("No pattern set.")
            } else {
                Pattern.fromString(patternStr)
                when (scope) {
                    PatternScope.GLOBAL -> updateConfigPatterns(Config.globalConfig.patterns)
                    PatternScope.SERVER -> updateConfigPatterns(Config.serverConfig?.patterns)
                }
            }
        } catch (e: IllegalArgumentException) {
            showMessage("Error initializing pattern \"${patternStr}\": ${e.message}")
        } catch (e: IndexOutOfBoundsException) {
            showMessage("Error storing pattern \"${patternStr}\": ${e.message} @ $patternIndex")
        }

        this.closeGui(true)
    }

    private fun updateConfigPatterns(options: PatternOption?) {
        val list = options?.patternOptionValues ?: return

        val patternValues = list.toMutableList()
        val patternValue = PatternOption.PatternOptionValue(name, patternStr, true)

        if (replace) {
            patternValues[patternIndex] = patternValue
        } else {
            patternValues.add(patternIndex, patternValue)
        }

        options.patternOptionValues = patternValues

        Config.reloadPatterns(Config.serverConfig!!)
    }

    private fun cancel() {
        this.closeGui(true)
    }

    private fun testPattern() {
        try {
            if (patternStr == "") {
                showMessage("No pattern to test.")
                return
            }

            val pattern = Pattern.fromString(patternStr)
            val CSP = pattern.toCharStylePair(testMsgStr)?.toMutableList()

            if (CSP == null) {
                showMessage("This pattern didn't match the test message.")
                return
            }

            this.resultText = ChatProcessor.aggregateCSPs(CSP)
        } catch (e: IllegalArgumentException) {
            showMessage("Error initializing pattern \"${patternStr}\": ${e.message}")
        }
    }

    private fun createTextWidget(x: Int, y: Int, text: Text): TextWidget {
        val textWidget = TextWidget(x, y, -1, 10, text, this.textRenderer)
        this.addTextWidget(textWidget)

        return textWidget
    }

    private fun createTextLabel(x: Int, y: Int, text: String): WidgetLabel {
        val labelWidth = getStringWidth(text)
        val label = WidgetLabel(x, y, labelWidth, 10, 0xFFFFFF, text)
        this.addWidget(label)

        return label
    }

    private fun createTextField(
        x: Int,
        y: Int,
        width: Int,
        text: String,
        listener: ITextFieldListener<GuiTextFieldGeneric>
    ): GuiTextFieldGeneric {
        val textField = GuiTextField(x, y, width, stdWidgetHeight, this.textRenderer)
        textField.text = text
        this.addTextField(textField, listener)

        return textField
    }

    private fun createButton(x: Int, y: Int, text: String, listener: IButtonActionListener): ButtonGeneric {
        val buttonWidth = getStringWidth(text) + stdMargin + 2
        val button = ButtonGeneric(x, y, buttonWidth, stdWidgetHeight, text)
        this.addButton(button, listener)

        return button
    }
}
