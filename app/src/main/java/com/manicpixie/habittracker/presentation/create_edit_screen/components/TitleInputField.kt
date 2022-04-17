package com.manicpixie.habittracker.presentation.create_edit_screen.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.presentation.habits_list_screen.components.HabitItem
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.clearFocusOnKeyboardDismiss
import com.manicpixie.habittracker.util.dpToSp



@Composable
fun TitleInputField(
    isHintVisible: Boolean = true,
    keyboardOptions: KeyboardOptions,
    hint: String = "",
    text: String = "",
    onFocusChange: (FocusState) -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = true
) {

    Row(
        modifier = modifier
            .height(55.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = text,
                maxLines = 2,
                cursorBrush = SolidColor(PrimaryBlack),
                onValueChange = onValueChange,
                keyboardOptions = keyboardOptions,
                singleLine = singleLine,
                textStyle = textStyle,
                visualTransformation = visualTransformation,
                modifier = Modifier
                    .clearFocusOnKeyboardDismiss()
                    .fillMaxWidth()
                    .onFocusChanged { onFocusChange(it) }

            )
            Spacer(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(1.2.dp)
                    .fillMaxWidth()
                    .background(PrimaryBlack)
            )
            if (isHintVisible) {
                Text(
                    text = hint,
                    style = textStyle
                )
            }
        }
    }

}