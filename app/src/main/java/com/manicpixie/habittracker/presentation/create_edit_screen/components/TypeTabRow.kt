package com.manicpixie.habittracker.presentation.create_edit_screen.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.util.noRippleClickable


enum class HabitType {
    Good, Bad
}

@Composable
fun TypeTabRow(
    habitType: HabitType,
    onTabSelected: (habitType: HabitType) -> Unit,
    modifier: Modifier = Modifier
) {

    val density = LocalDensity.current
    val goodFontColor =
        animateColorAsState(targetValue = if (habitType == HabitType.Good) White else PrimaryBlack)
    val badFontColor =
        animateColorAsState(targetValue = if (habitType == HabitType.Bad) White else PrimaryBlack)
    var sizeTopBar by remember { mutableStateOf(Size.Zero) }
    var isClickedOnce by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = PrimaryBlack,
                shape = RoundedCornerShape(20.dp)
            )
            .height(38.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                sizeTopBar = coordinates.size.toSize()
            }
            .clip(RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {

        TabIndicator(
            habitType,
            width = with(density) { sizeTopBar.width.toDp() },
            numberOfTabs = HabitType.values().size,
            isClickedOnce = isClickedOnce
        )
        Row(
            modifier = Modifier
                .height(38.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TypeTab(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.good_type),
                onClick = {
                    if (!isClickedOnce) isClickedOnce = true
                    onTabSelected(HabitType.Good)
                },
                fontColor = goodFontColor.value
            )
            TypeTab(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.bad_type),
                onClick = {
                    if (!isClickedOnce) isClickedOnce = true
                    onTabSelected(HabitType.Bad)
                },
                fontColor = badFontColor.value
            )

        }
    }

}


@Composable
fun TypeTab(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    fontColor: Color
) {
    Box(
        modifier = modifier.noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 19.dp),
                color = fontColor
            ),
            text = title
        )
    }
}


@Composable
private fun TabIndicator(
    habitType: HabitType,
    numberOfTabs: Int,
    width: Dp,
    isClickedOnce: Boolean
) {
    val transition = updateTransition(
        habitType,
        label = stringResource(id = R.string.tab_indicator)
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (HabitType.Good isTransitioningTo HabitType.Bad) {
                spring(stiffness = Spring.StiffnessVeryLow)

            } else {
                if (isClickedOnce) spring(stiffness = Spring.StiffnessMedium)
                else snap()
            }
        },
        label = stringResource(id = R.string.indicator_left)
    ) { page ->
        page.ordinal * width / numberOfTabs
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (HabitType.Good isTransitioningTo HabitType.Bad) {
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                if (isClickedOnce) spring(stiffness = Spring.StiffnessVeryLow) else
                    snap()
            }
        },
        label = stringResource(id = R.string.indicator_right)
    ) { page ->
        width / numberOfTabs * (page.ordinal + 1)
    }

    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .clip(RoundedCornerShape(20.dp))
            .width(indicatorRight - indicatorLeft)
            .fillMaxSize()
            .background(PrimaryBlack)
    )
}