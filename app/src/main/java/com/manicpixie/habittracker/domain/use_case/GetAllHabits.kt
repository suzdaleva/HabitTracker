package com.manicpixie.habittracker.domain.use_case

import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.util.HabitOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllHabits @Inject constructor(
    private val repository: HabitRepository
) {

    operator fun invoke(habitOrder: HabitOrder = HabitOrder.ByDate): Flow<List<HabitEntity>> {
        return repository.getAllHabits().map { habits ->
            when(habitOrder) {
                is HabitOrder.ByDate -> habits.sortedBy { it.date }.reversed()
                is HabitOrder.ByPriority -> habits.sortedBy { it.priority }.reversed()
            }
        }
    }
}

