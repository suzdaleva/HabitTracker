package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.noRippleClickable



@Composable
fun ExpandButton(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(30.dp)
            .border(width = 1.dp, color = PrimaryBlack, shape = CircleShape)
            .clip(CircleShape)
            .noRippleClickable { onClick() }
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(14.dp)
                .rotate(if (expanded) 180f else 0f),
            painter = painterResource(id = R.drawable.down_arrow),
            contentDescription = stringResource(id = R.string.expand_button_description)
        )
    }
}