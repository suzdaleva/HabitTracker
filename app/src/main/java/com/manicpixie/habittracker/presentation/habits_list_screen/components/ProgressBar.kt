package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.ui.theme.PrimaryBlack

@Composable
fun ProgressBar(
    progress: Int
){
    Box(
        modifier = Modifier
            .height(12.dp)
            .width(120.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Transparent)
            .border(width = 1.dp, color = PrimaryBlack, shape = RoundedCornerShape(5.dp))
    )
}