package com.manicpixie.habittracker.presentation.habits_list_screen

import androidx.compose.ui.focus.FocusState
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.util.HabitOrder

sealed class HabitListEvent {
    data class EnteredQuery(val value: String, val habitOrder: HabitOrder) : HabitListEvent()
    data class ChangeQueryFocus(val focusState: FocusState) : HabitListEvent()
    data class Sort(val habitOrder: HabitOrder) : HabitListEvent()
    data class CountHabit(val habit: Habit, val habitOrder: HabitOrder) : HabitListEvent()
    data class LoadNextItems(val habitOrder: HabitOrder) : HabitListEvent()
}


