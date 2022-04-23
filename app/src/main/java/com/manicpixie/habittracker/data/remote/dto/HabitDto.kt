package com.manicpixie.habittracker.data.remote.dto


import com.google.gson.annotations.SerializedName


data class HabitDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("date")
    val date: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("done_dates")
    val doneDates: List<Int>,
    @SerializedName("frequency")
    val frequency: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("uid")
    val uid: String? = null
)