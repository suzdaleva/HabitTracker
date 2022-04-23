package com.manicpixie.habittracker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Habit(
    var title: String,
    var description: String,
    var priority: Int,
    var type: Int,
    var count: Int,
    val dateOfCreation: Long,
    var numberOfRepetitions: Int,
    var targetNumberOfDays: Int,
    val averagePerformance: Float,
    val todayPerformance: Int,
    val numberOfCheckedDays: Int,
) : Parcelable
