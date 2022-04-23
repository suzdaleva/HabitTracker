package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.dpToSp


@Composable
fun SortRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = modifier.size(15.dp),
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryBlack,
                unselectedColor = PrimaryBlack
            )
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            color = PrimaryBlack,
            text = text,
            style = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 18.dp),
            )
        )
    }

}
