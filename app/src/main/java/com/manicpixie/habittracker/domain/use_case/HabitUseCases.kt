package com.manicpixie.habittracker.domain.use_case

data class HabitUseCases(
    val addHabit: AddHabit,
    val deleteHabit: DeleteHabit,
    val getAllHabits: GetAllHabits,
    val getHabit: GetHabit
)
