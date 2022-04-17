package com.manicpixie.habittracker.di

import android.app.Application
import androidx.room.Room
import com.manicpixie.habittracker.data.local.HabitDatabase
import com.manicpixie.habittracker.data.repository.HabitRepositoryImpl
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(app: Application): HabitDatabase {
        return Room.databaseBuilder(
            app, HabitDatabase::class.java, "habit_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideHabitRepository(database: HabitDatabase): HabitRepository {
        return HabitRepositoryImpl(database.dao)
    }

    @Provides
    @Singleton
    fun provideHabitUseCases(repository: HabitRepository): HabitUseCases {
        return HabitUseCases(
            getHabit = GetHabit(repository),
            deleteHabit = DeleteHabit(repository),
            addHabit = AddHabit(repository),
            getAllHabits = GetAllHabits(repository)
        )
    }
}