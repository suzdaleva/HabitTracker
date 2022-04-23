package com.manicpixie.habittracker.presentation.create_edit_screen


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.domain.use_case.HabitUseCases
import com.manicpixie.habittracker.domain.util.InvalidHabitException
import com.manicpixie.habittracker.domain.util.ResourceProvider
import com.manicpixie.habittracker.presentation.create_edit_screen.components.HabitType
import com.manicpixie.habittracker.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject



@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases,
    savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _habitTitle = mutableStateOf(
        TextFieldState(
            hint = resourceProvider.getString(R.string.habit_title_hint),
        )
    )
    val habitTitle: State<TextFieldState> = _habitTitle

    private val _habitDescription = mutableStateOf(
        TextFieldState(
            hint = resourceProvider.getString(R.string.habit_description_hint)
        )
    )
    val habitDescription: State<TextFieldState> = _habitDescription

    private val _numberOfRepetitions = mutableStateOf(
        TextFieldState(
            hint = resourceProvider.getString(R.string.number_input_hint)
        )
    )
    val numberOfRepetitions: State<TextFieldState> = _numberOfRepetitions

    private val _numberOfDays = mutableStateOf(
        TextFieldState(
            hint = resourceProvider.getString(R.string.number_input_hint)
        )
    )
    val numberOfDays: State<TextFieldState> = _numberOfDays

    private val _habitType = mutableStateOf(HabitType.Good)
    val habitType: State<HabitType> = _habitType

    private val _habitPriority = mutableStateOf(0)
    val habitPriority: State<Int> = _habitPriority

    private val _numberOfCheckedDays = mutableStateOf(0)
    val numberOfCheckedDays: State<Int> = _numberOfCheckedDays

    private val _count = mutableStateOf(0)
    val count: State<Int> = _count


    private var currentHabit: Habit? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private val _averagePerformance = mutableStateOf(0.00f)
    val averagePerformance: State<Float> = _averagePerformance

    init {
        viewModelScope.launch {
            val receivedHabit = savedStateHandle.get<Habit>("habit") ?: return@launch
            _habitTitle.value = enteredPlainText(receivedHabit.title, habitTitle.value)
            _habitDescription.value =
                enteredPlainText(receivedHabit.description, habitDescription.value)
            _numberOfRepetitions.value =
                enteredPlainText(
                    receivedHabit.numberOfRepetitions.toString(),
                    numberOfRepetitions.value
                )
            _numberOfDays.value =
                enteredPlainText(receivedHabit.targetNumberOfDays.toString(), numberOfDays.value)
            _habitPriority.value = receivedHabit.priority
            _habitType.value = when (receivedHabit.type) {
                0 -> HabitType.Good
                else -> HabitType.Bad
            }
            _averagePerformance.value = receivedHabit.averagePerformance
            _count.value = receivedHabit.count
            _numberOfCheckedDays.value = receivedHabit.numberOfCheckedDays
            currentHabit = receivedHabit

        }
    }


    private fun saveHabit() {
        viewModelScope.launch {
            try {
                val newHabit = Habit(
                    title = habitTitle.value.text,
                    description = habitDescription.value.text,
                    type = habitType.value.ordinal,
                    priority = habitPriority.value,
                    dateOfCreation = Calendar.getInstance().timeInMillis,
                    count = 0,
                    numberOfRepetitions = if (numberOfRepetitions.value.text.isNotBlank()) numberOfRepetitions.value.text.toInt() else 1,
                    targetNumberOfDays = if (numberOfDays.value.text.isNotBlank()) numberOfDays.value.text.toInt() else 1,
                    numberOfCheckedDays = 0,
                    averagePerformance = 0.00f,
                    todayPerformance = 0
                )

                habitUseCases.addHabit(newHabit)
                currentHabit = newHabit
                _eventFlow.emit(UiEvent.SaveNote)
            } catch (e: InvalidHabitException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: resourceProvider.getString(R.string.snackbar_default_message),
                        buttonText = resourceProvider.getString(R.string.snackbar_button_text)
                    )
                )
            }
        }
    }

    private fun deleteHabit() {
            viewModelScope.launch {
                if (currentHabit != null){
                habitUseCases.deleteHabit(currentHabit!!)
                _eventFlow.emit(UiEvent.SaveNote)
                } else _eventFlow.emit(UiEvent.SaveNote)
            }
    }

    private fun updateHabit() {
        viewModelScope.launch {
            try {
                currentHabit?.run {
                    title = habitTitle.value.text
                    description = habitDescription.value.text
                    priority = habitPriority.value
                    type = habitType.value.ordinal
                    targetNumberOfDays =
                        if (numberOfDays.value.text.isNotBlank()) numberOfDays.value.text.toInt() else 0
                }
                currentHabit?.numberOfRepetitions =
                    if (numberOfRepetitions.value.text.isNotBlank()) numberOfRepetitions.value.text.toInt() else 0
                habitUseCases.updateHabit(currentHabit!!)
                _eventFlow.emit(UiEvent.SaveNote)
            } catch (e: InvalidHabitException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: resourceProvider.getString(R.string.snackbar_default_message),
                        buttonText = resourceProvider.getString(R.string.snackbar_button_text)
                    )
                )
            }
        }
    }


    fun onEvent(event: CreateEditEvent) {
        when (event) {
            is CreateEditEvent.EnteredHabitTitle -> {
                _habitTitle.value = enteredPlainText(event.value, habitTitle.value)

            }
            is CreateEditEvent.ChangeHabitTitleFocus -> {
                _habitTitle.value = changeFocus(event.focusState, habitTitle.value)
            }
            is CreateEditEvent.EnteredHabitDescription -> {
                _habitDescription.value = enteredPlainText(event.value, habitDescription.value)
            }
            is CreateEditEvent.ChangeHabitDescriptionFocus -> {
                _habitDescription.value = changeFocus(event.focusState, habitDescription.value)

            }
            is CreateEditEvent.EnteredNumberOfRepetitions -> {
                _numberOfRepetitions.value =
                    enteredNumericText(event.value, numberOfRepetitions.value)

            }
            is CreateEditEvent.ChangeNumberOfRepetitionsFocus -> {
                _numberOfRepetitions.value =
                    changeFocus(event.focusState, numberOfRepetitions.value)

            }
            is CreateEditEvent.EnteredNumberOfDays -> {
                _numberOfDays.value = enteredNumericText(event.value, numberOfDays.value)
            }
            is CreateEditEvent.ChangeNumberOfDaysFocus -> {
                _numberOfDays.value = changeFocus(event.focusState, numberOfDays.value)
            }
            is CreateEditEvent.SetHabitPriority -> {
                _habitPriority.value = event.priority
            }
            is CreateEditEvent.SetHabitType -> {
                _habitType.value = event.habitType
            }
            is CreateEditEvent.SaveHabit -> {
                if (currentHabit != null) updateHabit() else saveHabit()
            }
            is CreateEditEvent.DeleteHabit -> {
                deleteHabit()
            }

        }
    }

}


