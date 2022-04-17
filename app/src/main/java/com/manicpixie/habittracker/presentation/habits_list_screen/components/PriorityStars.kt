package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R

@Composable
fun PriorityStars(
    size: Dp,
    priority: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(size),
            painter = painterResource(id = R.drawable.filled_star),
            contentDescription = null
        )
        Image(
            modifier = Modifier.size(size),
            painter = painterResource(id = if (priority >= 1) R.drawable.filled_star else R.drawable.unfilled_star),
            contentDescription = null
        )
        Image(
            modifier = Modifier.size(size),
            painter = painterResource(id = if (priority == 2) R.drawable.filled_star else R.drawable.unfilled_star),
            contentDescription = null
        )
    }

}