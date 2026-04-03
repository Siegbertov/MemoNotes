package com.s1g1.memonotes.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import com.s1g1.memonotes.ui.ThemePrefs

class SettingsViewModel(application: Application): AndroidViewModel(application){

    private val themePrefs = ThemePrefs(application)

    fun toggleDarkMode(isEnabled: Boolean) {
        themePrefs.isDarkMode = isEnabled
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun getSavedTheme() : Boolean = themePrefs.isDarkMode

}