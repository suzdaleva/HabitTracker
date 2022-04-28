package com.manicpixie.habittracker.data.remote.dto



import com.google.gson.annotations.SerializedName
import com.manicpixie.habittracker.data.local.entity.HabitEntity


data class HabitDto(
    @SerializedName("count")
    val count: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("description")
    val description: String,
    @SerializedName("done_dates")
    val doneDates: List<Long>,
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
) {
    fun toHabitEntity(): HabitEntity {
        return HabitEntity(
            title = title,
            description = description,
            priority = priority,
            type = type,
            frequency = frequency,
            countPerDay = count,
            dateOfCreation = date.toLong(),
            habitUid = uid,
            doneDates = doneDatesMapping(doneDates)
        )
    }


    //Mapping dates when habits were performed to their quantity
    private fun doneDatesMapping(doneDates: List<Long>): MutableMap<Long, Int> {
        val map = mutableMapOf<Long, Int>()
        if (doneDates.isNotEmpty()) {
            val keys = doneDates.distinct()
            keys.onEach { key ->
                val value = doneDates.filter {
                    it == key
                }.size
                map[key] = value
            }
        }
        return map
    }
}