package com.manicpixie.habittracker.domain.util

sealed class HabitOrder{
    object ByDate: HabitOrder()
    object ByPriority: HabitOrder()
}
