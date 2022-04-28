package com.manicpixie.habittracker.domain.repository


import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.util.HabitOrder
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    suspend fun insert(habit: HabitEntity)

    suspend fun update(habit: HabitEntity)

    suspend fun increaseCount(habit: HabitEntity)

    suspend fun deleteHabit(habit: HabitEntity)

    fun loadNextHabits(
        habitOrder: HabitOrder,
        page: Int,
        pageSize: Int,
        shouldUpdateRemote: Boolean
    ): Flow<Result<List<HabitEntity>>>

    fun getHabits(habitOrder: HabitOrder, listSize: Int, query: String): Flow<Result<List<HabitEntity>>>

    fun searchHabits(habitOrder: HabitOrder, query: String): Flow<Result<List<HabitEntity>>>

    suspend fun getHabitByDate(date: Long): HabitEntity

    suspend fun getHabitsInfo(): List<Any>

}