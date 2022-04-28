package com.manicpixie.habittracker.domain.use_case


import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.HabitOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadNextHabits @Inject constructor(
    private val repository: HabitRepository
) {

    operator fun invoke(
        habitOrder: HabitOrder,
        page: Int,
        pageSize: Int,
        shouldUpdateRemote: Boolean
    ): Flow<Result<List<Habit>>> {
        return repository.loadNextHabits(habitOrder, page, pageSize, shouldUpdateRemote)
            .map { result ->
                result.map { habits ->
                    habits.map {

                        it.toHabit()
                    }

                }
            }
    }
}

