package com.manicpixie.habittracker.presentation.create_edit_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.presentation.create_edit_screen.components.*
import com.manicpixie.habittracker.presentation.destinations.HabitsListScreenDestination
import com.manicpixie.habittracker.ui.theme.*
import com.manicpixie.habittracker.util.AppButton
import com.manicpixie.habittracker.util.AppSnackBar
import com.manicpixie.habittracker.util.dpToSp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest


@Destination
@Composable
fun CreateEditScreen(
    habit: HabitEntity?,
    navigator: DestinationsNavigator,
    createEditViewModel: CreateEditViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    val habitType = createEditViewModel.habitType.value
    val habitTitle = createEditViewModel.habitTitle.value
    val habitDescription = createEditViewModel.habitDescription.value
    val habitPriority = createEditViewModel.habitPriority.value
    val numberOfRepetitions = createEditViewModel.numberOfRepetitions.value
    val numberOfDays = createEditViewModel.numberOfDays.value
    val averageFrequency = createEditViewModel.averageFrequency.value
    val backgroundColor by animateColorAsState(if (habitType == HabitType.Good) White else White)

    LaunchedEffect(true) {
        createEditViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CreateEditViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.value.showSnackbar(
                        message = event.message,
                        actionLabel = event.buttonText
                    )
                }
                is CreateEditViewModel.UiEvent.SaveNote -> {
                    navigator.navigate(HabitsListScreenDestination())
                }
            }

        }
    }


    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(White)
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(
                    height = 38.dp,
                    width = 142.dp,
                    buttonText = "Удалить".uppercase(),
                    backgroundColor = PrimaryBlack,
                    fontColor = White,
                    onClick = {
                        createEditViewModel.onEvent(CreateEditEvent.DeleteHabit)
                        navigator.navigate(HabitsListScreenDestination())
                    },
                    fontSize = 18.dp,
                    letterSpacing = 0.07f,
                    borderWidth = 1.5.dp
                )

                AppButton(
                    height = 38.dp,
                    width = 152.dp,
                    buttonText = "Сохранить".uppercase(),
                    backgroundColor = White,
                    fontColor = PrimaryBlack,
                    onClick = { createEditViewModel.onEvent(CreateEditEvent.SaveHabit) },
                    fontSize = 18.dp,
                    letterSpacing = 0.07f,
                    borderWidth = 1.5.dp
                )
            }
        },
        topBar = {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Habit".uppercase(),
                    style = MaterialTheme.typography.caption,
                    fontSize = dpToSp(dp = 27.dp)
                )
                Spacer(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(1.6.dp)
                        .fillMaxWidth()
                        .background(PrimaryBlack)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier,
                hostState = snackbarHostState.value,
                snackbar = { snackbarData ->
                    AppSnackBar(snackbarData = snackbarData)
                }
            )
        }
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                Brush.radialGradient(
                    0.0f to GradientCyan,
                    0.7f to GradientLemon,
                    1.0f to White,
                    center = Offset(this.size.width - 100f, -350f)
                ), center = Offset(this.size.width - 100f, -350f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp, bottom = 63.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                TitleInputField(
                    isHintVisible = habitTitle.isHintVisible,
                    keyboardOptions = KeyboardOptions.Default,
                    hint = habitTitle.hint,
                    text = habitTitle.text,
                    onFocusChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.ChangeHabitTitleFocus(
                                it
                            )
                        )
                    },
                    modifier = Modifier,
                    visualTransformation = VisualTransformation.None,
                    onValueChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.EnteredHabitTitle(
                                it
                            )
                        )
                    },
                    textStyle = MaterialTheme.typography.h4.copy(
                        fontSize = dpToSp(dp = 24.dp)
                    ),
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                DropDownMenu(
                    selectedPriority = habitPriority,
                    onSelectPriority = {
                        createEditViewModel.onEvent(CreateEditEvent.SetHabitPriority(it))
                    })

                Spacer(modifier = Modifier.height(20.dp))

                TypeTabRow(
                    modifier = Modifier.padding(start = 40.dp),
                    backgroundColor = backgroundColor,
                    habitType = habitType,
                    onTabSelected = { createEditViewModel.onEvent(CreateEditEvent.SetHabitType(it)) }
                )

                Spacer(modifier = Modifier.height(20.dp))
                FrequencySection(numberOfRepetitions = numberOfRepetitions,
                    numberOfDays = numberOfDays,
                    onDaysFocusChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.ChangeNumberOfDaysFocus(
                                it
                            )
                        )
                    },
                    onDaysValueChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.EnteredNumberOfDays(
                                it
                            )
                        )
                    },
                    onRepetitionsFocusChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.ChangeNumberOfRepetitionsFocus(
                                it
                            )
                        )
                    },
                    onRepetitionsValueChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.EnteredNumberOfRepetitions(
                                it
                            )
                        )
                    })
            }

            Spacer(modifier = Modifier.height(25.dp))

            Column(verticalArrangement = Arrangement.SpaceBetween) {

                DescriptionBox(
                    modifier = Modifier.weight(1.7f),
                    isHintVisible = habitDescription.isHintVisible,
                    hint = habitDescription.hint,
                    text = habitDescription.text,
                    onFocusChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.ChangeHabitDescriptionFocus(
                                it
                            )
                        )
                    },
                    onValueChange = {
                        createEditViewModel.onEvent(
                            CreateEditEvent.EnteredHabitDescription(
                                it
                            )
                        )
                    },
                    textStyle = MaterialTheme.typography.h4.copy(
                        fontSize = dpToSp(dp = 19.dp)
                    )
                )

                Spacer(modifier = Modifier.height(25.dp))

                ProgressSection(
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.h4.copy(
                        fontSize = dpToSp(dp = 14.dp),
                        letterSpacing = 0.02.em
                    ),
                    numberOfDays = numberOfRepetitions.hint.toInt(),
                    numberOfTimes = numberOfDays.hint.toInt(),
                    performance = averageFrequency
                )
            }
        }
    }

}

