package com.manicpixie.habittracker.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manicpixie.habittracker.data.remote.dto.HabitDto
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.util.setMidnight
import java.util.*
import kotlin.math.roundToInt


@Entity
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "habit_title")
    var title: String,
    @ColumnInfo(name = "habit_description")
    var description: String,
    @ColumnInfo(name = "habit_priority")
    var priority: Int,
    @ColumnInfo(name = "habit_type")
    var type: Int,
    @ColumnInfo(name = "habit_number_of_repetitions")
    var numberOfRepetitions: Int,
    @ColumnInfo(name = "habit_target_number_of_days")
    var targetNumberOfDays: Int,
    @ColumnInfo(name = "done_dates")
    val doneDates: MutableMap<Long, Int>,
    @ColumnInfo(name = "habit_date_of_creation")
    val dateOfCreation: Long,
    @ColumnInfo(name = "habit_uid")
    var habitUid: String? = null
) {
    fun toHabit(): Habit {
        return Habit(
            title = title,
            description = description,
            priority = priority,
            type = type,
            count = if (doneDates.isEmpty()) 0 else doneDates.values.sum(),
            dateOfCreation = dateOfCreation,
            numberOfRepetitions = numberOfRepetitions,
            targetNumberOfDays = targetNumberOfDays,
            averagePerformance = if (doneDates.isNotEmpty()) (doneDates.values.sum()
                .toFloat() * targetNumberOfDays.toFloat()) * 100 / (doneDates.size.toFloat() * numberOfRepetitions.toFloat()) else 0f,
            todayPerformance = if (doneDates.isNotEmpty() &&
                doneDates.containsKey(Calendar.getInstance().setMidnight().timeInMillis)
            )
                doneDates[Calendar.getInstance().setMidnight().timeInMillis]!! else 0,
            numberOfCheckedDays = if (doneDates.isEmpty()) 0 else doneDates.size,
        )
    }

    fun toHabitDto(): HabitDto {
        return HabitDto(
            title = title,
            description = description,
            priority = priority,
            type = type,
            count = if (doneDates.isEmpty()) 0 else doneDates.values.sum(),
            date = Calendar.getInstance().timeInMillis.toInt(),
            frequency = if (targetNumberOfDays != 0) (numberOfRepetitions.toFloat() / targetNumberOfDays.toFloat()).roundToInt()
            else numberOfRepetitions,
            doneDates = emptyList(),
            uid = habitUid
        )
    }
}

