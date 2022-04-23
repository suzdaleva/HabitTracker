package com.manicpixie.habittracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeleteDto(
    @SerializedName("uid")
    val uid: String
)
