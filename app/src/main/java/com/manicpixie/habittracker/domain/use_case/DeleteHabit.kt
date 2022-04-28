package com.manicpixie.habittracker.domain.use_case



import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabit @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        val currentHabit = repository.getHabitByDate(habit.dateOfCreation)
        repository.deleteHabit(currentHabit)
    }
}