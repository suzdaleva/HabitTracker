package com.manicpixie.habittracker.presentation.create_edit_screen

import androidx.compose.ui.focus.FocusState
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.presentation.create_edit_screen.components.HabitType


sealed class CreateEditEvent{
    data class EnteredHabitTitle(val value: String) : CreateEditEvent()
    data class ChangeHabitTitleFocus(val focusState: FocusState) : CreateEditEvent()
    data class EnteredHabitDescription(val value: String) : CreateEditEvent()
    data class ChangeHabitDescriptionFocus(val focusState: FocusState) : CreateEditEvent()
    data class SetHabitPriority(val priority: Int) : CreateEditEvent()
    data class SetHabitType(val habitType: HabitType) : CreateEditEvent()
    data class EnteredNumberOfRepetitions(val value: String) : CreateEditEvent()
    data class ChangeNumberOfRepetitionsFocus(val focusState: FocusState) : CreateEditEvent()
    data class EnteredNumberOfDays(val value: String) : CreateEditEvent()
    data class ChangeNumberOfDaysFocus(val focusState: FocusState) : CreateEditEvent()
    object SaveHabit: CreateEditEvent()
    object DeleteHabit: CreateEditEvent()
}



