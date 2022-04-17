package com.manicpixie.habittracker.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class HabitEntity(
    @ColumnInfo(name = "habit_color")
    val color: Int,
    @ColumnInfo(name = "habit_count")
    val count: Int,
    @ColumnInfo(name = "habit_date")
    val date: Long,
    @ColumnInfo(name = "habit_description")
    val description: String,
    @ColumnInfo(name = "habit_doneDates")
    val doneDates: Int,
    @ColumnInfo(name = "habit_frequency")
    val frequency: Int,
    @ColumnInfo(name = "habit_priority")
    val priority: Int,
    @ColumnInfo(name = "habit_title")
    val title: String,
    @ColumnInfo(name = "habit_type")
    val type: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
) : Parcelable
