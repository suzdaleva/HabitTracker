package com.manicpixie.habittracker.presentation.habits_list_screen

import androidx.compose.ui.focus.FocusState
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.util.HabitOrder

sealed class HabitListEvent{
    data class EnteredQuery(val value: String, val habitOrder: HabitOrder) : HabitListEvent()
    data class ChangeQueryFocus(val focusState: FocusState) : HabitListEvent()
    data class Sort(val habitOrder: HabitOrder): HabitListEvent()
    data class DoHabit(val habit: HabitEntity): HabitListEvent()
}


