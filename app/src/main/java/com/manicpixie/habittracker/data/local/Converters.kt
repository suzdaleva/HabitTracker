package com.manicpixie.habittracker.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {

    @TypeConverter
    fun fromString(value: String?): Map<Long?, Int?>? {
        val mapType: Type = object : TypeToken<Map<Long?, Int?>?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<Long?, Int?>?): String? {
        val gson = Gson()
        return gson.toJson(map)
    }
}
