package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.formatPercentage
import com.manicpixie.habittracker.util.setTextForDays
import com.manicpixie.habittracker.util.setTextForRepetitions
import kotlin.math.*


@Composable
fun ProgressSection(
    textStyle: TextStyle = TextStyle(),
    numberOfDays: Int,
    numberOfTimes: Int,
    performance: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.average_result),
                style = textStyle
            )
            Text(
                text = formatPercentage((performance * 100.0).roundToInt() / 100f),
                style = textStyle
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(
            modifier = Modifier
                .height(26.dp)
                .fillMaxWidth()
        ) {
            drawRoundRect(
                brush = SolidColor(PrimaryBlack),
                size = size, cornerRadius = CornerRadius(50f),
                style = Stroke(3f, cap = StrokeCap.Round)
            )
            val startX = size.width / 26
            repeat(min((0.12 * performance).roundToInt(), 12))
            {
                drawLine(
                    brush = SolidColor(PrimaryBlack),
                    start = Offset(startX + (size.width / 13 * it), size.height),
                    end = Offset(startX + (it + 1) * size.width / 13, 0f),
                    strokeWidth = 3f
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$numberOfDays ${setTextForDays(numberOfDays.toString())}/$numberOfTimes ${
                setTextForRepetitions(
                    numberOfTimes.toString()
                )
            }",
            style = textStyle
        )


    }
}