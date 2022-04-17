package com.manicpixie.habittracker.domain.use_case

import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.InvalidHabitException
import javax.inject.Inject


class AddHabit @Inject constructor(
    private val repository: HabitRepository
) {

    @Throws(InvalidHabitException::class)
    suspend operator fun invoke(habit: HabitEntity) {
        if(habit.title.isBlank()) {
            throw InvalidHabitException("Необходимо ввести название")
        }
        if(habit.description.isBlank()) {
            throw InvalidHabitException("Необходимо ввести описание")
        }
        repository.insert(habit)
    }
}