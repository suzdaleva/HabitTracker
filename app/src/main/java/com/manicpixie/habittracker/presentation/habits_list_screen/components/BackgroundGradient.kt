package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.manicpixie.habittracker.ui.theme.GradientCyan
import com.manicpixie.habittracker.ui.theme.GradientLemon
import com.manicpixie.habittracker.ui.theme.White

@Composable
fun BackgroundGradient(
    modifier: Modifier = Modifier
){
    Canvas(modifier = modifier.fillMaxSize()) {
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
}