package com.manicpixie.habittracker.data.remote

import com.manicpixie.habittracker.data.remote.dto.HabitDto
import retrofit2.http.*

interface HabitApi {


    @GET("habit")
    suspend fun getHabits(): List<HabitDto>


    @Headers("Content-Type: application/json")
    @PUT("habit")
    suspend fun postHabits(@Body habit: String) : HabitDto


    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body uid: String)

    @Headers("Content-Type: application/json")
    @POST("habit_done")
    suspend fun increaseHabitCount(@Body habit: String)


    companion object {
        const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
        const val AUTHORIZATION_TOKEN = "199408f4-348c-4ae3-84fd-3e16348a37d3"
    }
}