package com.manicpixie.habittracker.domain.use_case

import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabit @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: HabitEntity) {
        repository.deleteHabit(habit)
    }
}