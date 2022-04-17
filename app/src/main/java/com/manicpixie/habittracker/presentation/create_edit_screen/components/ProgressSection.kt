package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import kotlin.math.roundToInt


@Composable
fun ProgressSection(
    textStyle: TextStyle = TextStyle(),
    numberOfDays: Int,
    numberOfTimes: Int,
    performance: Float,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "средняя частота за день",
                style = textStyle
            )
            Text(
                text = "${(performance * 100.0).roundToInt() / 100.0}%",
                style = textStyle
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(Color.Transparent)
                .border(width = 1.dp, color = PrimaryBlack, shape = RoundedCornerShape(13.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$numberOfDays дней/$numberOfTimes раз",
            style = textStyle
        )


    }
}