package com.manicpixie.habittracker.domain.repository

import com.manicpixie.habittracker.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun insert(habit: HabitEntity)
    suspend fun insertHabits(habits: List<HabitEntity>)
    suspend fun update(habit:HabitEntity)
    suspend fun numberOfEntries(): Int
    suspend fun deleteHabit(habit: HabitEntity)
    suspend fun get(key: Long) : HabitEntity
    fun getAllHabits() : Flow<List<HabitEntity>>
    suspend fun getThisHabit(): HabitEntity
    suspend fun clear()
    suspend fun getHabitById(key: Long): HabitEntity
}