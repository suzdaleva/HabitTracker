package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.habittracker.util.TextFieldState
import com.manicpixie.habittracker.util.dpToSp
import com.manicpixie.habittracker.util.setTextForDays
import com.manicpixie.habittracker.util.setTextForRepetitions

@Composable
fun FrequencySection(
    numberOfRepetitions: TextFieldState,
    numberOfDays: TextFieldState,
    onRepetitionsFocusChange: (FocusState) -> Unit,
    onRepetitionsValueChange: (String) -> Unit,
    onDaysFocusChange: (FocusState) -> Unit,
    onDaysValueChange: (String) -> Unit,

    ) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        FrequencyInputField(
            label = setTextForRepetitions(numberOfRepetitions.text),
            textFieldState = numberOfRepetitions,
            onFocusChange = { onRepetitionsFocusChange(it) },
            onValueChange = { onRepetitionsValueChange(it) },
            textStyle = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 17.dp),
                letterSpacing = 0.04.em
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            modifier = Modifier.padding(top = 3.dp),
            style = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 20.dp),
                letterSpacing = 0.04.em,
                textAlign = TextAlign.Center
            ),
            text = "x",
        )
        Spacer(modifier = Modifier.width(6.dp))
        FrequencyInputField(
            textFieldState = numberOfDays,
            label = setTextForDays(numberOfDays.text),
            onFocusChange = { onDaysFocusChange(it) },
            onValueChange = { onDaysValueChange(it) },
            textStyle = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 17.dp),
                letterSpacing = 0.04.em
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}