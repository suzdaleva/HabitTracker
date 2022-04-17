package com.manicpixie.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manicpixie.habittracker.data.local.entity.HabitEntity



//@TypeConverters(Converters::class)
@Database(entities = [HabitEntity::class], version = 1)
abstract class HabitDatabase : RoomDatabase(){
    abstract val dao: HabitDao
}