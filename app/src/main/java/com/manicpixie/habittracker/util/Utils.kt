package com.manicpixie.habittracker.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import java.util.*
import com.manicpixie.habittracker.R

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }


inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

class Constants {
    companion object {
        const val PAGESIZE = 7
    }
}

val todayDateFormatted = GregorianCalendar.getInstance().also {
    it.timeZone = TimeZone.getTimeZone("GMT")
}.setMidnight().timeInMillis




fun enteredPlainText(value: String, textFieldState: TextFieldState): TextFieldState {
    return textFieldState.copy(text = value)
}

fun enteredNumericText(value: String, textFieldState: TextFieldState): TextFieldState {
    return textFieldState.copy(text = if (value.isDigitsOnly()) value else textFieldState.text)
}


fun Calendar.setMidnight() = this.apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun changeFocus(
    focusState: FocusState,
    textFieldState: TextFieldState
): TextFieldState {
    return textFieldState.copy(
        isHintVisible = !focusState.isFocused && textFieldState.text.isBlank()
    )
}

fun Modifier.drawColoredShadow(
    color: Color,
    alpha: Float = 0.8f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 10.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = this.drawBehind {
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = 0.0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )


        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            borderRadius.toPx(),
            borderRadius.toPx(),
            paint
        )


    }
}


fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom
    return keypadHeight > screenHeight * 0.15
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
}

fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {

    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val isKeyboardOpen by rememberIsKeyboardOpen()

        val focusManager = LocalFocusManager.current
        LaunchedEffect(isKeyboardOpen) {
            if (isKeyboardOpen) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}


@Composable
fun setTextForDays(numberOfDays: String): String {
    return when {
        numberOfDays.isBlank() -> stringResource(id = R.string.day_first_form)
        numberOfDays.endsWith("11") || numberOfDays.endsWith("12") || numberOfDays.endsWith("13") || numberOfDays.endsWith(
            "14"
        ) -> stringResource(id = R.string.day_third_form)
        numberOfDays.endsWith('1') -> stringResource(id = R.string.day_first_form)
        numberOfDays.endsWith('2') || numberOfDays.endsWith('3') || numberOfDays.endsWith('4') -> stringResource(id = R.string.day_second_form)
        else -> stringResource(id = R.string.day_third_form)
    }
}



@Composable
fun setTextForRepetitions(numberOfRepetitions: String): String {
    return when {
        numberOfRepetitions.endsWith("11") || numberOfRepetitions.endsWith("12") || numberOfRepetitions.endsWith(
            "13"
        ) || numberOfRepetitions.endsWith("14") -> stringResource(id = R.string.count_first_form)
        numberOfRepetitions.endsWith('2') || numberOfRepetitions.endsWith('3') || numberOfRepetitions.endsWith(
            '4'
        ) -> stringResource(id = R.string.count_second_form)
        else -> stringResource(id = R.string.count_first_form)
    }
}

fun setTextForRepetitions(numberOfRepetitions: String, context: Context): String {
    return when {
        numberOfRepetitions.endsWith("11") || numberOfRepetitions.endsWith("12") || numberOfRepetitions.endsWith(
            "13"
        ) || numberOfRepetitions.endsWith("14") -> context.getString(R.string.count_first_form)
        numberOfRepetitions.endsWith('2') || numberOfRepetitions.endsWith('3') || numberOfRepetitions.endsWith(
            '4'
        ) -> context.getString(R.string.count_second_form)
        else -> context.getString(R.string.count_first_form)
    }
}

fun formatPercentage(value: Float): String {
    return String.format("%.2f", value).replace(',', '.') + "%"
}