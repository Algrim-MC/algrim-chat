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

package mc.algrim.fabric.chat.gui.data

import mc.algrim.fabric.chat.config.option.PatternOption

sealed interface PatternListEntryData

sealed interface ScopedEntryData : PatternListEntryData {
    val scope: PatternScope
}

data class EmptyEntryData(override val scope: PatternScope) : ScopedEntryData

data class PatternEntryData(
    val value: PatternOption.PatternOptionValue,
    override val scope: PatternScope,
    val index: Int
) : ScopedEntryData

data class SeparatorEntryData(val value: String) : PatternListEntryData

enum class PatternScope {
    GLOBAL,
    SERVER
}
