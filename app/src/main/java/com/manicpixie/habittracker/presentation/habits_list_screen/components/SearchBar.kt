package com.manicpixie.habittracker.presentation.habits_list_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.manicpixie.habittracker.R
import com.manicpixie.habittracker.ui.theme.LightGray
import com.manicpixie.habittracker.ui.theme.PrimaryBlack
import com.manicpixie.habittracker.util.clearFocusOnKeyboardDismiss

@Composable
fun SearchBar(
    isHintVisible: Boolean = true,
    hint: String = "",
    text: String = "",
    onFocusChange: (FocusState) -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    onDismiss:() -> Unit
){
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
                    singleLine = true,
                    textStyle = textStyle,
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .clearFocusOnKeyboardDismiss()
                        .fillMaxWidth()
                        .onFocusChanged { onFocusChange(it) }

                )
            IconButton(
                modifier = Modifier.size(40.dp).align(Alignment.CenterEnd),
                onClick = onDismiss
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search_dismiss_icon),
                    contentDescription = "Dismiss"
                )
            }

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
                    style = textStyle.copy(
                        color = LightGray
                    )
                )
            }
        }
    }
}