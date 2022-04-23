package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.dpToSp


@Composable
fun CreateEditTopBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(70.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_title).uppercase(),
            style = MaterialTheme.typography.caption,
            color = PrimaryBlack,
            fontSize = dpToSp(dp = 27.dp)
        )
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(1.6.dp)
                .fillMaxWidth()
                .background(PrimaryBlack)
        )
    }
}