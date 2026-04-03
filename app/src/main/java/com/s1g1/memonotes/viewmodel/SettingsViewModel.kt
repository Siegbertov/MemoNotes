package com.s1g1.memonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel(){

    private val _isDarkMode = MutableLiveData<Boolean>(false)
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    fun toggleDarkMode(isEnabled: Boolean) {
        _isDarkMode.value = isEnabled
    }

}