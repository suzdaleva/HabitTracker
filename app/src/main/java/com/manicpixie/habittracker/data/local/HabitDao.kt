package com.manicpixie.habittracker.data.local

import androidx.room.*
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Update
    suspend fun update(habit:HabitEntity)

    @Query("SELECT count(*) FROM habitEntity")
    suspend fun numberOfEntries(): Int

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM HabitEntity WHERE id = :key")
    suspend fun get(key: Long) : HabitEntity

//    @Query("SELECT EXISTS (SELECT 1 FROM session WHERE session_date = :sessionDate)")
//    suspend fun sessionWithDateExists(sessionDate: Calendar): Boolean

    @Query("SELECT * FROM habitEntity ORDER BY id DESC")
    fun getAllHabits() : Flow<List<HabitEntity>>

    @Query("SELECT * FROM habitEntity ORDER BY id DESC LIMIT 1")
    suspend fun getThisHabit(): HabitEntity

    @Query("DELETE FROM habitEntity")
    suspend fun clear()

    @Query("SELECT * from habitEntity WHERE id = :key")
    suspend fun getHabitById(key: Long): HabitEntity
}