package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.AppButton
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.util.formatPercentage
import com.manicpixie.habittracker.util.noRippleClickable


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    height: Dp,
    showInformationPanel: Boolean,
    onCreate: () -> Unit,
    onInfoClick: () -> Unit,
    informationIconColor: Color,
    informationButtonColor: Color,
    totalCountOfHabits: Int,
    numberOfNegativeHabits: Int,
    numberOfPositiveHabits: Int,
    totalAveragePerformance: Float
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val averagePerformance = if (totalCountOfHabits != 0)
            formatPercentage(totalAveragePerformance) else "0.00%"
        if (showInformationPanel) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .align(
                        Alignment.TopCenter
                    ),
                text = stringResource(
                    id = R.string.information,
                    totalCountOfHabits,
                    numberOfNegativeHabits,
                    numberOfPositiveHabits,
                    averagePerformance
                ),
                color = PrimaryBlack,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = dpToSp(dp = 13.dp),
                    letterSpacing = 0.05.em,
                    lineHeight = 1.4.em
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .border(width = 1.5.dp, color = PrimaryBlack, shape = CircleShape)
                    .clip(CircleShape)
                    .noRippleClickable { onInfoClick() }
                    .background(informationButtonColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.info_icon),
                    contentDescription = stringResource(id = R.string.expand_button_description),
                    colorFilter = ColorFilter.tint(informationIconColor)
                )
            }
            AppButton(
                height = 38.dp,
                width = 130.dp,
                buttonText = stringResource(id = R.string.create_button_text).uppercase(),
                backgroundColor = White,
                fontColor = PrimaryBlack,
                onClick = onCreate,
                fontSize = 18.dp,
                letterSpacing = 0.07f,
                borderWidth = 1.5.dp
            )
        }
    }

    Spacer(
        modifier = Modifier
            .height(1.6.dp)
            .fillMaxWidth()
            .background(if (showInformationPanel) PrimaryBlack else White)
    )
}
