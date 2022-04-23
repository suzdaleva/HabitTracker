package com.manicpixie.habittracker.di

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.room.Room
import com.manicpixie.habittracker.data.local.HabitDatabase
import com.manicpixie.habittracker.data.remote.HabitApi
import com.manicpixie.habittracker.data.repository.HabitRepositoryImpl
import com.manicpixie.habittracker.domain.repository.HabitRepository
import com.manicpixie.habittracker.domain.use_case.*
import com.manicpixie.habittracker.domain.util.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject
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
    fun provideHabitRepository(database: HabitDatabase, api: HabitApi): HabitRepository {
        return HabitRepositoryImpl(api, database.dao)
    }

    @Provides
    @Singleton
    fun provideHabitUseCases(repository: HabitRepository, resourceProvider: ResourceProvider): HabitUseCases {
        return HabitUseCases(
            increaseHabitCount = IncreaseHabitCount(repository),
            deleteHabit = DeleteHabit(repository),
            addHabit = AddHabit(repository, resourceProvider),
            loadNextHabits = LoadNextHabits(repository),
            updateHabit = UpdateHabit(repository, resourceProvider),
            getHabits = GetHabits(repository),
            searchHabits = SearchHabits(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): HabitApi {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", HabitApi.AUTHORIZATION_TOKEN)
                .addHeader("accept", "application/json")
                .build()
            chain.proceed(newRequest)
        }).build()

        return Retrofit.Builder()
            .baseUrl(HabitApi.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HabitApi::class.java)
    }

    @Singleton
    @Provides
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ResourceProvider {
        return ResourceProvider(context)
    }
}