package com.manicpixie.habittracker.data.local

import androidx.room.TypeConverter


class Converters {
    @TypeConverter
    fun listToString(value: List<Int>): String = value.joinToString(separator = ".")

    @TypeConverter
    fun stringToList(value: String): List<Int> {
        val stringList = value.split(".")
        return listOf(
            stringList[1].toInt(), stringList[2].toInt()
        )
    }
}
