package com.manicpixie.habittracker.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
data class Habit(
    val color: Int,
    val count: Int,
    val date: Int,
    val description: String,
    val doneDates: List<Int>,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int,
): Parcelable
