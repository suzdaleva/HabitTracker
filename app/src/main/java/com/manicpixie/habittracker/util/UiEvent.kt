package com.manicpixie.habittracker.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val buttonText: String) : UiEvent()
    object SaveNote : UiEvent()
}
