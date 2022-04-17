package com.manicpixie.habittracker.presentation.create_edit_screen.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.clearFocusOnKeyboardDismiss

@Composable
fun DescriptionBox(
    modifier : Modifier = Modifier,
    isHintVisible: Boolean = true,
    hint: String = "",
    text: String = "",
    onFocusChange: (FocusState) -> Unit,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle()
) {
    Box(modifier = modifier.fillMaxSize()
        //.height(200.dp)
        .border(width = 1.dp, color = PrimaryBlack)) {
        BasicTextField(
            value = text,
            cursorBrush = SolidColor(PrimaryBlack),
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier
                .clearFocusOnKeyboardDismiss()
                .fillMaxWidth()
                .padding(12.dp)
                .onFocusChanged { onFocusChange(it) }

        )
        if (isHintVisible) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = hint,
                style = textStyle
            )
        }
    }
}