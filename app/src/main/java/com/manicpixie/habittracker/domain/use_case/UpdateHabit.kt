package com.manicpixie.habittracker.domain.use_case


import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.InvalidHabitException
import com.manicpixie.habittracker.domain.util.ResourceProvider
import javax.inject.Inject
import com.manicpixie.habittracker.R
import java.util.*

class UpdateHabit @Inject constructor(
    private val repository: HabitRepository,
    private val resourceProvider: ResourceProvider
) {
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
        val currentHabit = repository.getHabitByDate(habit.dateOfCreation)
        currentHabit.run {
            title = habit.title
            description = habit.description
            priority = habit.priority
            type = habit.type
            frequency = habit.frequency
            countPerDay = habit.countPerDay
            dateOfCreation = GregorianCalendar.getInstance().also {
                it.timeZone = TimeZone.getTimeZone("GMT")
            }.timeInMillis
        }
        repository.update(habit = currentHabit)
    }
}
