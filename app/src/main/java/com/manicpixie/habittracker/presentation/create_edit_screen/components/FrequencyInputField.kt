package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.TextFieldState
import com.manicpixie.habittracker.util.clearFocusOnKeyboardDismiss
import com.manicpixie.habittracker.util.dpToSp


@Composable
fun FrequencyInputField(
    textFieldState: TextFieldState,
    keyboardOptions: KeyboardOptions,
    onFocusChange: (FocusState) -> Unit,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    textStyle: TextStyle = TextStyle(),
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.border(width = 1.dp, color = PrimaryBlack),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = textFieldState.text,
                cursorBrush = SolidColor(PrimaryBlack),
                onValueChange = onValueChange,
                keyboardOptions = keyboardOptions,
                singleLine = singleLine,
                textStyle = textStyle,
                modifier = Modifier
                    .clearFocusOnKeyboardDismiss()
                    .width(IntrinsicSize.Min)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .onFocusChanged { onFocusChange(it) }

            )
            if (textFieldState.isHintVisible) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    text = textFieldState.hint,
                    style = textStyle
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            style = MaterialTheme.typography.h4.copy(
                fontSize = dpToSp(dp = 17.dp)
            ),
            text = label,
        )

    }

}