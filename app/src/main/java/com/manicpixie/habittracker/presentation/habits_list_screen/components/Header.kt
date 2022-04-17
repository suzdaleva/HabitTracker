package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.util.dpToSp

@Composable
fun Header(
    height: Dp,
    modifier: Modifier = Modifier,
    onSort: () -> Unit,
    onSearch: () -> Unit
) {
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart),
            text = "Habit".uppercase(),
            style = MaterialTheme.typography.caption,
            fontSize = dpToSp(dp = 27.dp)
        )
        Row(
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.CenterEnd)
        ) {
            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = onSearch
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "Search"
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = onSort
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sort_icon),
                    contentDescription = "Sort"
                )
            }
        }
    }
}