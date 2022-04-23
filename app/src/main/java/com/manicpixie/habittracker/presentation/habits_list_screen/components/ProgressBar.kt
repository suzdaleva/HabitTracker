package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun ProgressBar(
    performance: Float
) {
    Canvas(
        modifier = Modifier
            .width(140.dp)
            .height(12.dp)
            .fillMaxWidth()
    ) {
        drawRoundRect(
            brush = SolidColor(PrimaryBlack),
            size = size, cornerRadius = CornerRadius(50f),
            style = Stroke(2.3f, cap = StrokeCap.Round)
        )
        val startX = size.width / 26
        repeat(min((0.12 * performance).roundToInt(), 12))
        {
            drawLine(
                brush = SolidColor(PrimaryBlack),
                start = Offset(startX + (size.width / 13 * it), size.height),
                end = Offset(startX + (it + 1) * size.width / 13, 0f),
                strokeWidth = 2.3f
            )
        }
    }
}