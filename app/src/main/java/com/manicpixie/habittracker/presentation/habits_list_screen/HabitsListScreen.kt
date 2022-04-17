package com.manicpixie.habittracker.presentation.habits_list_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import com.manicpixie.habittracker.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.presentation.create_edit_screen.components.SortDialog
import com.manicpixie.habittracker.presentation.destinations.CreateEditScreenDestination
import com.manicpixie.habittracker.presentation.habits_list_screen.components.HabitItem
import com.manicpixie.habittracker.presentation.habits_list_screen.components.Header
import com.manicpixie.habittracker.presentation.habits_list_screen.components.SearchBar
import com.manicpixie.habittracker.ui.theme.GradientCyan
import com.manicpixie.habittracker.ui.theme.GradientLemon
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.AppButton
import com.manicpixie.habittracker.util.AppSnackBar
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.util.noRippleClickable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.roundToInt


@Destination(start = true)
@Composable
fun HabitsListScreen(
    navigator: DestinationsNavigator,
    habitsListViewModel: HabitsListViewModel = hiltViewModel()
) {


    val queryState = habitsListViewModel.searchQuery.value
    val habits = habitsListViewModel.habitsListState.value.habits
    var expandedHabit by remember { mutableStateOf<Int?>(null) }
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    val toolbarHeight = 70.dp
    val density = LocalDensity.current.density
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val showSortDialog = remember { mutableStateOf(false) }
    val showSearchBar = remember { mutableStateOf(false) }
    val currentHabitOrder = habitsListViewModel.habitsListState.value.habitOrder

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                Log.i(
                    "info",
                    "${(toolbarOffsetHeightPx.value / density).roundToInt().dp + toolbarHeight}   ${toolbarOffsetHeightPx.value.roundToInt().dp}"
                )
                return Offset.Zero
            }
        }
    }

    val listState = rememberLazyListState()

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
                .nestedScroll(nestedScrollConnection)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    Brush.radialGradient(
                        0.0f to GradientCyan,
                        0.5f to GradientLemon,
                        1.0f to White,
                        center = Offset(this.size.width - 50f, this.size.height / 3.5f),
                        radius = this.size.minDimension / 1.7f
                    ),
                    center = Offset(this.size.width - 50f, this.size.height / 3.5f),
                    radius = this.size.minDimension / 1.7f
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
                    Column(){
                        Spacer(
                            modifier = Modifier
                                .height(1.6.dp)
                                .fillMaxWidth()
                                .background(if(showSearchBar.value) White else PrimaryBlack)
                        )
                        AnimatedVisibility(
                            visible = showSearchBar.value
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
                                    onDismiss = { showSearchBar.value = !showSearchBar.value },
                                    onFocusChange = { habitsListViewModel.onEvent(HabitListEvent.ChangeQueryFocus(it))},
                                    onValueChange = { habitsListViewModel.onEvent(HabitListEvent.EnteredQuery(it, currentHabitOrder))} )
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
                items(count = habits.size) { habit ->
                    HabitItem(
                        habit = habits[habit],
                        expanded = expandedHabit == habit,
                        onEdit = { navigator.navigate(CreateEditScreenDestination(habit = habits[habit])) },
                        onExpand = {
                            expandedHabit = if (expandedHabit == habit) null else habit
                        })
                }

            }

            Header(
                modifier = Modifier
                    .graphicsLayer {
                    if ((listState.firstVisibleItemIndex == 0 || listState.firstVisibleItemIndex == 1) && listState.firstVisibleItemScrollOffset.toFloat() < 70.dp.value) {
                        translationY = -toolbarOffsetHeightPx.value
                        alpha = 1 - (-toolbarOffsetHeightPx.value / 20f)


                    } else {
                        translationY = -400.dp.value
                        alpha = 0f
                    }
                        if(showSearchBar.value){
                            translationY = -400.dp.value
                            alpha = 0f
                        }
                },
                height = toolbarHeight,
                onSort = { showSortDialog.value = !showSortDialog.value },
                onSearch = {showSearchBar.value = true})


            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter), visible = true
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(White)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .border(width = 1.5.dp, color = PrimaryBlack, shape = CircleShape)
                            .clip(CircleShape)
                            .noRippleClickable { }
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.info_icon),
                            contentDescription = "Expand button"
                        )
                    }
                    AppButton(
                        height = 38.dp,
                        width = 130.dp,
                        buttonText = "Создать".uppercase(),
                        backgroundColor = White,
                        fontColor = PrimaryBlack,
                        onClick = { navigator.navigate(CreateEditScreenDestination()) },
                        fontSize = 18.dp,
                        letterSpacing = 0.07f,
                        borderWidth = 1.5.dp
                    )
                }
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