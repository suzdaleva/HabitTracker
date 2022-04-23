package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.domain.model.Habit
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.AppButton
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.util.formatPercentage
import com.manicpixie.habittracker.util.setTextForDays
import com.manicpixie.habittracker.R


@Composable
fun HabitItem(
    expanded: Boolean,
    onEdit: () -> Unit,
    onExpand: () -> Unit,
    onCountHabit: () -> Unit,
    habit: Habit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = stringResource(id = if (habit.type == 0) R.string.positive_type_tag else R.string.negative_type_tag),
                style = MaterialTheme.typography.h3,
                fontSize = dpToSp(dp = 14.dp)

            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                ProgressBar(performance = habit.todayPerformance * 100 * habit.targetNumberOfDays.toFloat() / habit.numberOfRepetitions.toFloat())
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (habit.numberOfRepetitions != 0) formatPercentage(habit.todayPerformance * 100 * habit.targetNumberOfDays.toFloat() / habit.numberOfRepetitions.toFloat())
                    else "0.00%",
                    style = MaterialTheme.typography.h4,
                    fontSize = dpToSp(dp = 14.dp)

                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 50.dp),
            text = habit.title.uppercase(),
            style = MaterialTheme.typography.caption,
            fontSize = dpToSp(dp = 27.dp)

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(0.dp, (-10).dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = habit.description,
                style = MaterialTheme.typography.body1,
                fontSize = dpToSp(dp = 16.dp),
                overflow = if (expanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 12.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            ExpandButton(
                modifier = Modifier.weight(1f),
                expanded = expanded,
                onClick = onExpand
            )
        }
        if (expanded) {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    PriorityStars(priority = habit.priority, size = 13.dp)
                    Spacer(
                        modifier = Modifier
                            .height(7.dp)
                    )
                    Text(
                        text = "${habit.numberOfRepetitions} х за ${habit.targetNumberOfDays} ${
                            setTextForDays(
                                habit.targetNumberOfDays.toString()
                            )
                        }",
                        style = MaterialTheme.typography.h4.copy(
                            letterSpacing = 0.04.em
                        ),
                        fontSize = dpToSp(dp = 12.dp)
                    )
                }
                Row() {
                    AppButton(
                        height = 33.dp,
                        width = 142.dp,
                        buttonText = stringResource(id = R.string.edit_button_text),
                        backgroundColor = PrimaryBlack,
                        fontColor = White,
                        onClick = onEdit,
                        fontSize = 14.dp,
                        letterSpacing = 0.03f,
                        borderWidth = 1.2.dp
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    AppButton(
                        height = 33.dp,
                        width = 100.dp,
                        buttonText = stringResource(id = R.string.perform_button_text),
                        backgroundColor = Color.Transparent,
                        fontColor = PrimaryBlack,
                        onClick = onCountHabit,
                        fontSize = 14.dp,
                        letterSpacing = 0.03f,
                        borderWidth = 1.2.dp
                    )
                }

            }
        }
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )
        Spacer(
            modifier = Modifier
                .height(1.6.dp)
                .fillMaxWidth()
                .background(PrimaryBlack)
        )
    }
}
