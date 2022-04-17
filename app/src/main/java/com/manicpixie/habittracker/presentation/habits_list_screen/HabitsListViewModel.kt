package com.manicpixie.habittracker.presentation.habits_list_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manicpixie.habittracker.domain.use_case.HabitUseCases
import com.manicpixie.habittracker.domain.util.HabitOrder
import com.manicpixie.habittracker.util.TextFieldState
import com.manicpixie.habittracker.util.changeFocus
import com.manicpixie.habittracker.util.enteredText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class HabitsListViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases
) : ViewModel() {

    private var job: Job? = null

    private val _habitsListState = mutableStateOf(HabitsListState())
    val habitsListState: State<HabitsListState> = _habitsListState


    private val _searchQuery = mutableStateOf(
        TextFieldState(
            hint = "Поиск"
        )
    )
    val searchQuery: State<TextFieldState> = _searchQuery


    init {
        getHabits(HabitOrder.ByDate)
    }


    private fun getHabits(habitOrder: HabitOrder) {
        cancelJob()
        job = habitUseCases.getAllHabits(habitOrder).onEach {
            _habitsListState.value = HabitsListState(
                habits = it ?: emptyList(),
                habitOrder = habitOrder
            )
        }.cancellable().launchIn(viewModelScope)
    }


    private fun searchHabits(query: String, habitOrder: HabitOrder) {
        cancelJob()
        job = habitUseCases.getAllHabits(habitOrder).onEach { result ->
            _habitsListState.value = HabitsListState(
                habits = result.filter {
                    it.title.lowercase().contains(query) || it.description.lowercase()
                        .contains(query) ||
                            it.title.contains(query) || it.description.contains(query)
                } ?: emptyList(),
                habitOrder = habitOrder
            )

        }.cancellable().launchIn(viewModelScope)
    }

    fun onEvent(event: HabitListEvent) {
        when (event) {
            is HabitListEvent.EnteredQuery -> {
                _searchQuery.value = enteredText(event.value, searchQuery.value)
                searchHabits(searchQuery.value.text, event.habitOrder)
            }
            is HabitListEvent.ChangeQueryFocus -> {
                _searchQuery.value = changeFocus(event.focusState, searchQuery.value)
            }
            is HabitListEvent.DoHabit -> {

            }
            is HabitListEvent.Sort -> {
                if (habitsListState.value.habitOrder::class == event.habitOrder::class) return
                _searchQuery.value = _searchQuery.value.copy(
                    text = ""
                )
                getHabits(event.habitOrder)
            }
        }

    }

    private fun cancelJob() {
        job?.cancel()
    }


}



