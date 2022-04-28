package com.manicpixie.habittracker.domain.use_case

data class HabitUseCases(
    val addHabit: AddHabit,
    val deleteHabit: DeleteHabit,
    val loadNextHabits: LoadNextHabits,
    val increaseHabitCount: IncreaseHabitCount,
    val updateHabit: UpdateHabit,
    val getHabits: GetHabits,
    val searchHabits: SearchHabits,
    val getInfoUseCase: GetInfoUseCase
)
