package com.manicpixie.habittracker.util

import android.service.autofill.OnClickAction
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.ui.theme.PrimaryBlack

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    height: Dp,
    width: Dp,
    buttonText: String,
    backgroundColor: Color,
    fontColor: Color,
    onClick: () -> Unit,
    fontSize: Dp,
    letterSpacing: Float,
    borderWidth: Dp
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .background(backgroundColor)
            .noRippleClickable { onClick() }
            .border(width = borderWidth, color = PrimaryBlack),
        contentAlignment = Alignment.Center
    ) {
        Text(
            color = fontColor,
            text = buttonText,
            style = MaterialTheme.typography.button.copy(
                letterSpacing = letterSpacing.em
            ),
            fontSize = dpToSp(dp = fontSize)
        )
    }

}