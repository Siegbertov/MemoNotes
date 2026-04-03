package com.s1g1.memonotes.ui

import android.content.Context
import androidx.core.content.edit

class ThemePrefs(context: Context) {
    private val prefs = context.getSharedPreferences("memo_prefs", Context.MODE_PRIVATE)

    var isDarkMode: Boolean
        get() = prefs.getBoolean("is_dark_mode", false)
        set(value) = prefs.edit { putBoolean("is_dark_mode", value) }
}