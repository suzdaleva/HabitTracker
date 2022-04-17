package com.manicpixie.habittracker.data.remote

import com.manicpixie.habittracker.data.remote.dto.HabitDto
import retrofit2.http.*

interface HabitApi {


    @Headers(
        "accept: application/json",
        "Authorization: $AUTHORIZATION_TOKEN"
    )
    @GET("habit")
    suspend fun getHabits(): List<HabitDto>

    @Headers(
        "accept: application/json",
        "Authorization: $AUTHORIZATION_TOKEN"
    )
    @PUT("habit")
    suspend fun putHabits(): ArrayList<HabitDto>


    @Headers(
        "accept: application/json",
        "Authorization: $AUTHORIZATION_TOKEN"
    )
    @DELETE("habit")
    suspend fun deleteHabits(): ArrayList<HabitDto>

    @Headers(
        "accept: application/json",
        "Authorization: $AUTHORIZATION_TOKEN"
    )
    @POST("habit_done")
    suspend fun doHabit(): ArrayList<HabitDto>

    companion object{
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/habit/"
        const val AUTHORIZATION_TOKEN = "199408f4-348c-4ae3-84fd-3e16348a37d3"
    }
}