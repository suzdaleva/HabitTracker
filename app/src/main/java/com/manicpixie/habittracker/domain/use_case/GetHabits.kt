package com.manicpixie.habittracker.domain.use_case

import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.HabitOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHabits @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(
        habitOrder: HabitOrder, listSize: Int, query: String
    ): Flow<Result<List<Habit>>> {
        return repository.getHabits(habitOrder, listSize, query).map { result ->
            result.map { habits ->
                habits.map { it.toHabit() }
            }
        }
    }
}