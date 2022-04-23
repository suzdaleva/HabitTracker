package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.manicpixie.habittracker.presentation.habits_list_screen.components.PriorityStars
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.ui.theme.White
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.R


@Composable
fun DropDownMenu(
    selectedPriority: Int,
    onSelectPriority: (Int) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val suggestions: List<String> = listOf(stringResource(id = R.string.low_priority),
        stringResource(id = R.string.average_priority), stringResource(id = R.string.high_priority))

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column() {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = PrimaryBlack,
                    shape = RoundedCornerShape(20.dp)
                )
                .height(38.dp)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .clip(RoundedCornerShape(20.dp))
                .background(White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.h4.copy(
                        fontSize = dpToSp(dp = 19.dp)
                    ),
                    text = stringResource(id = R.string.priority),
                    modifier = Modifier
                        .padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                PriorityStars(
                    priority = selectedPriority,
                    modifier = Modifier.padding(top = 2.dp),
                    size = 14.dp
                )
            }
            IconButton(
                content = {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(if (expanded) 180f else 0f),
                        painter = painterResource(id = R.drawable.down_arrow),
                        contentDescription = stringResource(id = R.string.down_button_description)
                    )
                },
                onClick = { expanded = !expanded })
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
        ) {
            suggestions.forEachIndexed { index, label ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onSelectPriority(index)
                }) {
                    MenuItem(label = label, index = index)

                }
            }
        }
    }
}


@Composable
fun MenuItem(
    label: String,
    index: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 19.dp)
            )

        )
        PriorityStars(
            priority = index,
            modifier = Modifier.padding(top = 2.dp),
            size = 14.dp
        )

    }
}


