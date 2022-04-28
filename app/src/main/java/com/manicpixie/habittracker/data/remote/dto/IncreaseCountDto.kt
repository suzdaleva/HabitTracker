package com.manicpixie.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IncreaseCountDto(
    val date: Long,
    @SerializedName("habit_uid")
    val habitUid: String
)
