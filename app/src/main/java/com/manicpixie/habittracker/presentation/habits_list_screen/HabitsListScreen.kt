package com.manicpixie.habittracker.presentation.habits_list_screen

import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.manicpixie.habittracker.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.manicpixie.habittracker.presentation.create_edit_screen.components.SortDialog
import com.manicpixie.habittracker.presentation.destinations.CreateEditScreenDestination
import com.manicpixie.habittracker.presentation.habits_list_screen.components.*
import com.manicpixie.habittracker.ui.theme.*
import com.manicpixie.habittracker.util.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt


@Destination(start = true)
@Composable
fun HabitsListScreen(
    navigator: DestinationsNavigator,
    habitsListViewModel: HabitsListViewModel = hiltViewModel()
) {


    val queryState = habitsListViewModel.searchQuery.value
    val habits = habitsListViewModel.habitsListState.value.habits
    val endReached = habitsListViewModel.habitsListState.value.endReached
    val isLoading = habitsListViewModel.habitsListState.value.isLoading
    val totalCountOfHabits = habitsListViewModel.totalCountOfHabits
    val numberOfPositiveHabits = habitsListViewModel.numberOfPositiveHabits
    val numberOfNegativeHabits = habitsListViewModel.numberOfNegativeHabits
    val totalAveragePerformance = habitsListViewModel.totalAveragePerformance
    var expandedHabit by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    val toolbarHeight = 70.dp
    val density = LocalDensity.current.density
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val showSortDialog = remember { mutableStateOf(false) }
    val showSearchBar = remember { mutableStateOf(false) }
    val currentHabitOrder = habitsListViewModel.habitsListState.value.habitOrder
    val activity = (LocalContext.current as? Activity)
    var showInformationPanel by remember {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        speed = 3f,
        iterations = LottieConstants.IterateForever
    )
    val color = remember { mutableStateOf(LoaderTint) }

    val dynamicProperties =
        rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                keyPath = arrayOf("**"),
                property = LottieProperty.COLOR_FILTER,
                callback = {
                    PorterDuffColorFilter(
                        color.value.toArgb(),
                        PorterDuff.Mode.SRC_ATOP
                    )
                })
        )
    val isScrolledUp = remember { mutableStateOf(true) }
    val informationButtonColor =
        animateColorAsState(targetValue = if (showInformationPanel) PrimaryBlack else White)
    val informationIconColor =
        animateColorAsState(targetValue = if (showInformationPanel) White else PrimaryBlack)
    val informationBoxHeight =
        animateDpAsState(targetValue = if (showInformationPanel) 210.dp else 70.dp)


    BackHandler {
        activity?.finish()
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                isScrolledUp.value = available.y >= 0
                return Offset.Zero
            }
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(true) {
        habitsListViewModel.eventFlow.collectLatest { event ->
            if (event is UiEvent.ShowSnackBar) {
                snackbarHostState.value.showSnackbar(
                    message = event.message,
                    actionLabel = event.buttonText
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier,
                hostState = snackbarHostState.value,
                snackbar = { snackbarData: SnackbarData ->
                    AppSnackBar(snackbarData = snackbarData)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .nestedScroll(nestedScrollConnection)
        ) {
            BackgroundGradient(modifier = Modifier.fillMaxSize())

            if (isLoading && habits.isEmpty()) {
                LottieAnimation(
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .align(Alignment.Center),
                    composition = composition,
                    progress = progress,
                    dynamicProperties = dynamicProperties
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    top = if (showSearchBar.value) 0.dp else
                        (toolbarOffsetHeightPx.value / density).roundToInt().dp + toolbarHeight
                )
            ) {
                item {
                    Column() {
                        Spacer(
                            modifier = Modifier
                                .height(1.6.dp)
                                .fillMaxWidth()
                                .background(if (showSearchBar.value) White else PrimaryBlack)
                        )
                        AnimatedVisibility(
                            visible = showSearchBar.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut(animationSpec = tween(durationMillis = 700)) + shrinkVertically(
                                animationSpec = tween(durationMillis = 700)
                            ),
                        ) {
                            Column() {
                                Spacer(modifier = Modifier.height(50.dp))
                                SearchBar(
                                    isHintVisible = queryState.isHintVisible,
                                    hint = queryState.hint,
                                    text = queryState.text,
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    textStyle = MaterialTheme.typography.h4.copy(
                                        fontSize = dpToSp(dp = 22.dp)
                                    ),
                                    onDismiss = {
                                        showSearchBar.value = !showSearchBar.value
                                    },
                                    onFocusChange = {
                                        habitsListViewModel.onEvent(
                                            HabitListEvent.ChangeQueryFocus(
                                                it
                                            )
                                        )
                                    },
                                    onValueChange = {
                                        habitsListViewModel.onEvent(
                                            HabitListEvent.EnteredQuery(
                                                it,
                                                currentHabitOrder
                                            )
                                        )
                                    })
                                Spacer(modifier = Modifier.height(45.dp))
                                Spacer(
                                    modifier = Modifier
                                        .height(1.6.dp)
                                        .fillMaxWidth()
                                        .background(PrimaryBlack)
                                )
                            }
                        }
                    }

                }
                items(count = habits.size) { index ->
                    if (index >= habits.size - 1 && !endReached && !isLoading && queryState.text.isBlank()) {
                        habitsListViewModel.onEvent(HabitListEvent.LoadNextItems(currentHabitOrder))
                    }
                    HabitItem(
                        habit = habits[index],
                        expanded = expandedHabit == index,
                        onCountHabit = {
                            habitsListViewModel.onEvent(
                                HabitListEvent.CountHabit(
                                    habits[index],
                                    currentHabitOrder
                                )
                            )
                        },
                        onEdit = { navigator.navigate(CreateEditScreenDestination(habit = habits[index])) },
                        onExpand = {
                            expandedHabit = if (expandedHabit == index) null else index
                        })
                }
                item {
                    if (isLoading && habits.size >= Constants.PAGESIZE) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryBlack,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }

            }

            Header(
                modifier = Modifier
                    .graphicsLayer {
                        if ((listState.firstVisibleItemIndex == 0 || listState.firstVisibleItemIndex == 1) && listState.firstVisibleItemScrollOffset.toFloat() < 40.dp.value) {
                            translationY = -toolbarOffsetHeightPx.value
                            alpha = 1 - (-toolbarOffsetHeightPx.value / 20f)


                        } else {
                            translationY = -400.dp.value
                            alpha = 0f
                        }
                        if (showSearchBar.value) {
                            translationY = -400.dp.value
                            alpha = 0f
                        }
                    },
                height = toolbarHeight,
                onSort = { showSortDialog.value = !showSortDialog.value },
                onSearch = { showSearchBar.value = true })

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                visible = isScrolledUp.value,
                enter = slideInVertically(
                    initialOffsetY = { 200 },
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { 200 },
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                )
            ) {
                BottomBar(
                    modifier = Modifier.fillMaxWidth(),
                    height = informationBoxHeight.value,
                    showInformationPanel = showInformationPanel,
                    onCreate = {
                        navigator.navigate(CreateEditScreenDestination())
                    },
                    onInfoClick = {
                        showInformationPanel = !showInformationPanel
                    },
                    informationIconColor = informationIconColor.value,
                    informationButtonColor = informationButtonColor.value,
                    totalAveragePerformance = totalAveragePerformance.value,
                    totalCountOfHabits = totalCountOfHabits.value,
                    numberOfNegativeHabits = numberOfNegativeHabits.value,
                    numberOfPositiveHabits = numberOfPositiveHabits.value
                )
            }

            if (showSortDialog.value) {
                SortDialog(
                    habitOrder = currentHabitOrder,
                    onCancel = { showSortDialog.value = !showSortDialog.value },
                    onOK = { habitsListViewModel.onEvent(HabitListEvent.Sort(it)) })
            }
        }
    }
}
