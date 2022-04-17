package com.manicpixie.habittracker.data.repository

import androidx.compose.material.rememberDismissState
import com.manicpixie.habittracker.data.local.HabitDao
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(val dao: HabitDao): HabitRepository{
    override suspend fun insert(habit: HabitEntity) {
        dao.insert(habit)
    }

    override suspend fun insertHabits(habits: List<HabitEntity>) {
        dao.insertHabits(habits)
    }

    override suspend fun update(habit: HabitEntity) {
        dao.update(habit)
    }

    override suspend fun numberOfEntries(): Int {
        return dao.numberOfEntries()
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        dao.deleteHabit(habit)
    }

    override suspend fun get(key: Long): HabitEntity {
        return dao.get(key)
    }

    override fun getAllHabits(): Flow<List<HabitEntity>> {
        return dao.getAllHabits()
    }

    override suspend fun getThisHabit(): HabitEntity {
        return dao.getThisHabit()
    }

    override suspend fun clear() {
        dao.clear()
    }

    override suspend fun getHabitById(key: Long): HabitEntity {
        return dao.getHabitById(key)
    }
}