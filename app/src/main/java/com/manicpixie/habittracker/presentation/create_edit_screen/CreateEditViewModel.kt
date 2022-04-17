package com.manicpixie.habittracker.presentation.create_edit_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.use_case.HabitUseCases
import com.manicpixie.habittracker.domain.util.InvalidHabitException
import com.manicpixie.habittracker.presentation.create_edit_screen.components.HabitType
import com.manicpixie.habittracker.presentation.habits_list_screen.HabitsListViewModel
import com.manicpixie.habittracker.util.TextFieldState
import com.manicpixie.habittracker.util.changeFocus
import com.manicpixie.habittracker.util.enteredText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _habitTitle = mutableStateOf(
        TextFieldState(
            hint = "Название..."
        )
    )
    val habitTitle: State<TextFieldState> = _habitTitle

    private val _habitDescription = mutableStateOf(
        TextFieldState(
            hint = "Описание..."
        )
    )
    val habitDescription: State<TextFieldState> = _habitDescription

    private val _numberOfRepetitions = mutableStateOf(
        TextFieldState(
            hint = "0"
        )
    )
    val numberOfRepetitions: State<TextFieldState> = _numberOfRepetitions

    private val _numberOfDays = mutableStateOf(
        TextFieldState(
            hint = "0"
        )
    )
    val numberOfDays: State<TextFieldState> = _numberOfDays

    private val _habitType = mutableStateOf(HabitType.Good)
    val habitType: State<HabitType> = _habitType

    private val _habitPriority = mutableStateOf(0)
    val habitPriority: State<Int> = _habitPriority


    private var currentHabit: HabitEntity? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackBar(val message: String, val buttonText: String) : UiEvent()
        object SaveNote : UiEvent()
    }

//    private val _numberOfRepetitions = mutableStateOf(0)
//    val numberOfRepetitions: State<Int> = _numberOfRepetitions
//
//    private val _numberOfDays= mutableStateOf(0)
//    val numberOfDays: State<Int> = _numberOfDays

    private val _averageFrequency = mutableStateOf(0.00f)
    val averageFrequency: State<Float> = _averageFrequency

    init {
        viewModelScope.launch {
            val receivedHabit = savedStateHandle.get<HabitEntity>("habit") ?: return@launch
            _habitTitle.value = enteredText(receivedHabit.title, habitTitle.value)
            _habitDescription.value = enteredText(receivedHabit.description, habitDescription.value)
            _numberOfRepetitions.value =
                enteredText(receivedHabit.count.toString(), numberOfRepetitions.value)
            _numberOfDays.value =
                enteredText(receivedHabit.doneDates.toString(), numberOfDays.value)
            _habitPriority.value = receivedHabit.priority
            _habitType.value = when (receivedHabit.type) {
                0 -> HabitType.Good
                else -> HabitType.Bad
            }
            currentHabit = receivedHabit

        }
    }


    private fun saveHabit() {
        viewModelScope.launch {
            try {
                val newHabit = HabitEntity(
                    color = 1,
                    count = 0,
                    date = Calendar.getInstance().timeInMillis,
                    description = habitDescription.value.text,
                    doneDates = 1,
                    frequency = 0,
                    priority = habitPriority.value,
                    title = habitTitle.value.text,
                    type = habitType.value.ordinal,
                    id = currentHabit?.id
                )
                habitUseCases.addHabit(newHabit)
                currentHabit = newHabit
                _eventFlow.emit(UiEvent.SaveNote)
            } catch (e: InvalidHabitException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: "Couldn't save note",
                        buttonText = "OK"
                    )
                )
            }
        }
    }

    private fun deleteHabit() {
        if (currentHabit != null) {
            Log.i("delete", "habit deleted $currentHabit")
            viewModelScope.launch {
                habitUseCases.deleteHabit(currentHabit!!)
            }
        }
    }


    fun onEvent(event: CreateEditEvent) {
        when (event) {
            is CreateEditEvent.EnteredHabitTitle -> {
                _habitTitle.value = enteredText(event.value, habitTitle.value)

            }
            is CreateEditEvent.ChangeHabitTitleFocus -> {
                _habitTitle.value = changeFocus(event.focusState, habitTitle.value)
            }
            is CreateEditEvent.EnteredHabitDescription -> {
                _habitDescription.value = enteredText(event.value, habitDescription.value)
            }
            is CreateEditEvent.ChangeHabitDescriptionFocus -> {
                _habitDescription.value = changeFocus(event.focusState, habitDescription.value)

            }
            is CreateEditEvent.EnteredNumberOfRepetitions -> {
                _numberOfRepetitions.value = enteredText(event.value, numberOfRepetitions.value)

            }
            is CreateEditEvent.ChangeNumberOfRepetitionsFocus -> {
                _numberOfRepetitions.value =
                    changeFocus(event.focusState, numberOfRepetitions.value)

            }
            is CreateEditEvent.EnteredNumberOfDays -> {
                _numberOfDays.value = enteredText(event.value, numberOfDays.value)
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
                saveHabit()
            }
            is CreateEditEvent.DeleteHabit -> {
                deleteHabit()
            }

        }
    }

}


