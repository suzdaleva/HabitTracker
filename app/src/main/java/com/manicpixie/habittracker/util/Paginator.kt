package com.manicpixie.habittracker.util

import com.manicpixie.habittracker.domain.util.HabitOrder



interface Paginator<Key, Item> {
    suspend fun loadNextItems(habitOrder: HabitOrder, shouldUpdateRemote: Boolean)
    fun reset()
}