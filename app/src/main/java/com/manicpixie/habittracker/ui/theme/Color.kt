package com.manicpixie.habittracker.ui.theme


import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val PrimaryBlack = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFC4C4C4)
val GradientCyan = Color(0xFF72FFF7)
val GradientLemon = Color(0x66ECFFD4)


val  backgroundGradient =  Brush.radialGradient(
    0.0f to GradientCyan,
    0.6f to GradientLemon,
    1.0f to White,
)