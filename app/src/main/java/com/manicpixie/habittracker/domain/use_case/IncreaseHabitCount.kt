package com.manicpixie.habittracker.domain.use_case


import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.util.setMidnight
import java.util.*
import javax.inject.Inject

class IncreaseHabitCount @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        val currentHabit = repository.getHabitByDate(habit.dateOfCreation)
        val todayTime = Calendar.getInstance().setMidnight().timeInMillis
        if (!currentHabit.doneDates.containsKey(todayTime)) currentHabit.doneDates[todayTime] =
            1
        else {
            val currentValue = currentHabit.doneDates[todayTime] ?: 0
            currentHabit.doneDates[todayTime] = currentValue + 1
        }
        repository.increaseCount(currentHabit)
    }
}