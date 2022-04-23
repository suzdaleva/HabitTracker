package com.manicpixie.habittracker.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarData
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White

@Composable
fun AppSnackBar(
    snackbarData: SnackbarData
) {
    Card(
        backgroundColor = PrimaryBlack,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .height(135.dp)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.TopStart),
                text = snackbarData.message,
                color = Color.White,
                style = MaterialTheme.typography.h4.copy(
                    letterSpacing = 0.05.em
                ),
                fontSize = dpToSp(dp = 14.dp)
            )
            AppButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                height = 33.dp,
                width = 60.dp,
                buttonText = snackbarData.actionLabel ?: "OK",
                backgroundColor = White,
                fontColor = PrimaryBlack,
                onClick = { snackbarData.performAction() },
                fontSize = 14.dp,
                letterSpacing = 0.03f,
                borderWidth = 1.2.dp
            )
        }
    }

}