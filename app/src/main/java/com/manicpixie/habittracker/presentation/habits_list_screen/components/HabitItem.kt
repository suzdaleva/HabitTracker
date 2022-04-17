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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.data.local.entity.HabitEntity
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.AppButton
import com.manicpixie.habittracker.util.dpToSp
import kotlin.math.exp

@Composable
fun HabitItem(
    expanded: Boolean,
    onEdit: () -> Unit,
    onExpand: () -> Unit,
   // onDo: () -> Unit,
    habit: HabitEntity
) {
    Column(
        modifier = Modifier
            //.background(White)
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
                text = if(habit.type == 0) "positive" else "negative",
                style = MaterialTheme.typography.h3,
                fontSize = dpToSp(dp = 14.dp)

            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                ProgressBar(progress = 0)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = habit.frequency.toString(),
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
                .padding(horizontal= 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
                Text(
                    text = habit.description,
                    style = MaterialTheme.typography.body1,
                    fontSize = dpToSp(dp = 16.dp),
                    overflow = if (expanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    modifier = Modifier.weight(1f).padding(top = 12.dp)
                )
            Spacer(modifier = Modifier.width(10.dp))
            ExpandButton(modifier = Modifier.weight(1f),
                expanded = expanded,
                onClick = onExpand)
        }
        if (expanded) {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    PriorityStars(priority = habit.priority, size  = 13.dp)
                    Spacer(
                        modifier = Modifier
                            .height(7.dp)
                    )
                    Text(
                        text = "2 х в 4 дня",
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
                        buttonText = "Редактировать",
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
                        buttonText = "Выполнить",
                        backgroundColor = Color.Transparent,
                        fontColor = PrimaryBlack,
                        onClick = { /*TODO*/ },
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
