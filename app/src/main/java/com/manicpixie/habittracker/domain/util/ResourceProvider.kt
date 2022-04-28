package com.manicpixie.habittracker.domain.util

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(
    private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getContext(): Context {
        return context
    }
}