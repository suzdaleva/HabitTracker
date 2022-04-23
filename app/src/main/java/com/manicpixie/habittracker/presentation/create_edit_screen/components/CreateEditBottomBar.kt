package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.AppButton


@Composable
fun CreateEditBottomBar(
    onDelete: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(White)
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppButton(
            height = 38.dp,
            width = 142.dp,
            buttonText = stringResource(id = R.string.delete_button_text).uppercase(),
            backgroundColor = PrimaryBlack,
            fontColor = White,
            onClick = onDelete,
            fontSize = 18.dp,
            letterSpacing = 0.07f,
            borderWidth = 1.5.dp
        )
        AppButton(
            height = 38.dp,
            width = 152.dp,
            buttonText = stringResource(id = R.string.save_button_text).uppercase(),
            backgroundColor = White,
            fontColor = PrimaryBlack,
            onClick = onSave,
            fontSize = 18.dp,
            letterSpacing = 0.07f,
            borderWidth = 1.5.dp
        )
    }
}