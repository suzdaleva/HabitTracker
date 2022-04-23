package com.manicpixie.habittracker.data.local

import androidx.room.*
import com.manicpixie.habittracker.data.local.entity.HabitEntity


@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM habitEntity ORDER BY id DESC")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM habitEntity ORDER BY id DESC LIMIT 1")
    suspend fun getThisHabit(): HabitEntity

    @Query("SELECT * from habitEntity WHERE id = :key")
    suspend fun getHabitById(key: Long): HabitEntity

    @Query("SELECT * from habitEntity WHERE habit_date_of_creation = :date")
    suspend fun getHabitByDate(date: Long): HabitEntity
}