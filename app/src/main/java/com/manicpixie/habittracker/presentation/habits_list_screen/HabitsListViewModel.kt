package com.manicpixie.habittracker.presentation.habits_list_screen


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.use_case.HabitUseCases
import com.manicpixie.habittracker.domain.util.HabitOrder
import com.manicpixie.habittracker.domain.util.ResourceProvider
import com.manicpixie.habittracker.presentation.create_edit_screen.components.HabitType
import com.manicpixie.habittracker.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HabitsListViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var job: Job? = null

    private val _habitsListState = mutableStateOf(HabitsListState())
    val habitsListState: State<HabitsListState> = _habitsListState

    private val _totalCountOfHabits = mutableStateOf(0)
    val totalCountOfHabits: State<Int> = _totalCountOfHabits

    private val _numberOfPositiveHabits = mutableStateOf(0)
    val numberOfPositiveHabits: State<Int> = _numberOfPositiveHabits

    private val _numberOfNegativeHabits = mutableStateOf(0)
    val numberOfNegativeHabits: State<Int> = _numberOfNegativeHabits


    private val _totalAveragePerformance = mutableStateOf(0f)
    val totalAveragePerformance: State<Float> = _totalAveragePerformance

    private val _searchQuery = mutableStateOf(
        TextFieldState(
            hint = resourceProvider.getString(R.string.search_hint)
        )
    )
    val searchQuery: State<TextFieldState> = _searchQuery

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private val paginator = HabitPaginator(
        initialKey = habitsListState.value.page,
        onLoadUpdated = {
            _habitsListState.value = habitsListState.value.copy(
                isLoading = it
            )
        },
        onRequest = { nextPage, habitOrder, shouldUpdateRemote ->
            habitUseCases.loadNextHabits(
                habitOrder,
                nextPage,
                Constants.PAGESIZE,
                shouldUpdateRemote
            )
        },
        onSuccess = { habits, newKey, habitOrder ->
            _habitsListState.value = habitsListState.value.copy(
                habitOrder = habitOrder,
                habits = habitsListState.value.habits + habits,
                page = newKey,
                endReached = habits.isEmpty(),
            )
        },
        getNextKey = {
            _habitsListState.value.page + 1
        }
    )


    init {
        loadNextItems(HabitOrder.ByDate, shouldUpdateRemote = true)
    }

    private fun updateHabitsInfo() {
        viewModelScope.launch {
            val habitsInfo = habitUseCases.getInfoUseCase()
            _totalCountOfHabits.value = habitsInfo[0] as Int
            _numberOfNegativeHabits.value = habitsInfo[1] as Int
            _numberOfPositiveHabits.value = habitsInfo[2] as Int
            _totalAveragePerformance.value = habitsInfo[3] as Float

        }
    }


    private fun getHabits(habitOrder: HabitOrder, listSize: Int, query: String) {
        habitUseCases.getHabits(habitOrder, listSize, query).onEach { result ->
            _habitsListState.value = habitsListState.value.copy(
                habits = result.getOrNull() ?: emptyList(),
                habitOrder = habitOrder
            )
        }.cancellable().launchIn(viewModelScope)
    }


    private fun increaseHabitCount(habitOrder: HabitOrder, habit: Habit) {
        viewModelScope.launch {
            habitUseCases.increaseHabitCount(habit)
            val remainingNumberOfHabits =
                (habit.countPerDay / habit.frequency) - habit.todayPerformance - 1
            val snackBarMessage = when (habit.type) {
                HabitType.Good.ordinal -> {
                    if (habit.todayPerformance < (habit.countPerDay / habit.frequency - 1)) resourceProvider.getContext()
                        .getString(
                            R.string.snackbar_good_habit_message_continue,
                            remainingNumberOfHabits,
                            setTextForRepetitions(
                                remainingNumberOfHabits.toString(),
                                resourceProvider.getContext()
                            )
                        )
                    else resourceProvider.getString(R.string.snackbar_good_habit_message_enough)
                }
                else -> {
                    if (habit.todayPerformance < (habit.countPerDay / habit.frequency - 1)) resourceProvider.getContext()
                        .getString(
                            R.string.snackbar_bad_habit_message_continue,
                            remainingNumberOfHabits,
                            setTextForRepetitions(
                                remainingNumberOfHabits.toString(),
                                resourceProvider.getContext()
                            )
                        )
                    else resourceProvider.getString(R.string.snackbar_bad_habit_message_enough)
                }
            }
            _eventFlow.emit(
                UiEvent.ShowSnackBar(
                    message = snackBarMessage,
                    buttonText = resourceProvider.getString(R.string.snackbar_button_text)
                )
            )
            getHabits(habitOrder, habitsListState.value.habits.size, searchQuery.value.text)
        }
    }


    private fun searchHabits(query: String, habitOrder: HabitOrder) {
        cancelJob()
        habitUseCases.searchHabits(habitOrder, query).onEach { result ->
            _habitsListState.value = habitsListState.value.copy(
                habits = result.getOrNull() ?: emptyList(),
                habitOrder = habitOrder,
                isLoading = false
            )
            if (query.isBlank()) {
                resetState()
                loadNextItems(habitOrder, shouldUpdateRemote = false)
            }
        }.cancellable().launchIn(viewModelScope)
    }


    fun onEvent(event: HabitListEvent) {
        when (event) {
            is HabitListEvent.EnteredQuery -> {
                _searchQuery.value = enteredPlainText(event.value, searchQuery.value)
                searchHabits(searchQuery.value.text, event.habitOrder)
            }
            is HabitListEvent.ChangeQueryFocus -> {
                _searchQuery.value = changeFocus(event.focusState, searchQuery.value)
            }
            is HabitListEvent.CountHabit -> {
                increaseHabitCount(event.habitOrder, event.habit)
                updateHabitsInfo()
            }
            is HabitListEvent.Sort -> {
                if (habitsListState.value.habitOrder::class == event.habitOrder::class) return
                _searchQuery.value = _searchQuery.value.copy(
                    text = ""
                )
                resetState()
                loadNextItems(event.habitOrder, shouldUpdateRemote = false)
            }
            is HabitListEvent.LoadNextItems -> {
                loadNextItems(event.habitOrder, shouldUpdateRemote = true)
                updateHabitsInfo()
            }
        }

    }

    private fun cancelJob() {
        job?.cancel()
    }

    private fun resetState() {
        _habitsListState.value = habitsListState.value.copy(
            habitOrder = HabitOrder.ByDate,
            habits = emptyList(),
            isLoading = false,
            endReached = false,
            page = 0
        )
        paginator.reset()
    }

    private fun loadNextItems(habitOrder: HabitOrder, shouldUpdateRemote: Boolean) {
        cancelJob()
        job = viewModelScope.launch {
            paginator.loadNextItems(habitOrder, shouldUpdateRemote)
            updateHabitsInfo()
        }
    }

}



