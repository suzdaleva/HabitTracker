package com.manicpixie.habittracker.domain.use_case

import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.InvalidHabitException
import com.manicpixie.habittracker.domain.util.ResourceProvider
import javax.inject.Inject
import com.manicpixie.habittracker.R


class AddHabit @Inject constructor(
    private val repository: HabitRepository,
    private val resourceProvider: ResourceProvider
) {

    @Throws(InvalidHabitException::class)
    suspend operator fun invoke(habit: Habit) {
        if (habit.title.isBlank()) {
            throw InvalidHabitException(resourceProvider.getString(R.string.snackbar_invalid_title_message))
        }
        if (habit.description.isBlank()) {
            throw InvalidHabitException(resourceProvider.getString(R.string.snackbar_invalid_description_message))
        }
        if (habit.countPerDay <= 0) {
            throw InvalidHabitException(resourceProvider.getString(R.string.snackbar_invalid_number_of_repetitions))
        }
        if (habit.frequency <= 0) {
            throw InvalidHabitException(resourceProvider.getString(R.string.snackbar_invalid_number_of_days))
        }
        val newHabit = HabitEntity(
            title = habit.title,
            description = habit.description,
            priority = habit.priority,
            type = habit.type,
            countPerDay = habit.countPerDay,
            frequency = habit.frequency,
            doneDates = mutableMapOf(),
            dateOfCreation = habit.dateOfCreation
        )
        repository.insert(newHabit)
    }
}