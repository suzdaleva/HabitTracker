package com.manicpixie.habittracker.presentation.habits_list_screen

import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.util.HabitOrder


data class HabitsListState(
    val habits: List<HabitEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val habitOrder: HabitOrder = HabitOrder.ByDate
)