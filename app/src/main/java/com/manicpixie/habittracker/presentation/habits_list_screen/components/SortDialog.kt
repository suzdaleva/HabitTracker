package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.manicpixie.habittracker.domain.util.HabitOrder
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.AppButton
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.util.drawColoredShadow
import com.manicpixie.habittracker.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SortDialog(
    habitOrder: HabitOrder,
    onCancel: () -> Unit,
    onOK: (HabitOrder) -> Unit
) {

    val selectedOrder = remember {
        mutableStateOf(habitOrder)
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(
            backgroundColor = White,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .height(204.dp)
                .background(White)
                .drawColoredShadow(
                    alpha = 0.2f,
                    shadowRadius = 10.dp,
                    color = PrimaryBlack,
                    borderRadius = 9.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.sort_by),
                    color = PrimaryBlack,
                    style = MaterialTheme.typography.h4.copy(
                        fontSize = dpToSp(dp = 24.dp)
                    ),
                )
                Column() {
                    SortRadioButton(
                        text = stringResource(id = R.string.sort_by_date),
                        selected = selectedOrder.value == HabitOrder.ByDate,
                        onSelect = { selectedOrder.value = HabitOrder.ByDate })
                    Spacer(modifier = Modifier.height(8.dp))
                    SortRadioButton(
                        text = stringResource(id = R.string.sort_by_priority),
                        selected = selectedOrder.value == HabitOrder.ByPriority,
                        onSelect = { selectedOrder.value = HabitOrder.ByPriority })
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    AppButton(
                        height = 33.dp,
                        width = 100.dp,
                        buttonText = stringResource(id = R.string.sort_cancel_button),
                        backgroundColor = PrimaryBlack,
                        fontColor = White,
                        onClick = onCancel,
                        fontSize = 14.dp,
                        letterSpacing = 0.03f,
                        borderWidth = 1.2.dp
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    AppButton(
                        height = 33.dp,
                        width = 60.dp,
                        buttonText = stringResource(id = R.string.sort_ok_button),
                        backgroundColor = Color.Transparent,
                        fontColor = PrimaryBlack,
                        onClick = {
                            onOK(selectedOrder.value)
                            onCancel()
                        },
                        fontSize = 14.dp,
                        letterSpacing = 0.03f,
                        borderWidth = 1.2.dp
                    )
                }
            }

        }
    }
}