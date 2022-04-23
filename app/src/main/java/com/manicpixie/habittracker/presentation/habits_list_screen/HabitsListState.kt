package com.manicpixie.habittracker.presentation.habits_list_screen


import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.util.HabitOrder


data class HabitsListState(
    val habits: List<Habit> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val habitOrder: HabitOrder = HabitOrder.ByDate,
    val endReached: Boolean = false,
    val page: Int = 0

)