package com.manicpixie.habittracker.presentation.habits_list_screen


import com.manicpixie.habittracker.domain.util.HabitOrder
import com.manicpixie.habittracker.util.Paginator
import kotlinx.coroutines.flow.*

class HabitPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key, habitOrder: HabitOrder, shouldUpdateRemote: Boolean) -> Flow<Result<List<Item>>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key, habitOrder: HabitOrder) -> Unit
) : Paginator<Key, Item> {
    private var currentKey = initialKey
    private var isMakingRequest = false
    override suspend fun loadNextItems(habitOrder: HabitOrder, shouldUpdateRemote: Boolean) {
        if (isMakingRequest) return
        isMakingRequest = true
        if (shouldUpdateRemote) {
            onLoadUpdated(true)
        }
        val result = onRequest(currentKey, habitOrder, shouldUpdateRemote)
        isMakingRequest = false
        var items: List<Item>?
        result.collect { result ->
            items = result.getOrNull() ?: emptyList()
            currentKey = getNextKey(items!!)
            onSuccess(items!!, currentKey, habitOrder)
            onLoadUpdated(false)
        }
    }

    override fun reset() {
        currentKey = initialKey
    }
}