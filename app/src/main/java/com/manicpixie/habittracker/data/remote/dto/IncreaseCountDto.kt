package com.manicpixie.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IncreaseCountDto(
    val date: Int,
    @SerializedName("habit_uid")
    val habitUid: String
)
