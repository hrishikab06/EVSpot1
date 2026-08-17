package com.example.evspot.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

enum class ThemeMode {
    LIGHT, DARK, DEFAULT
}

class ThemeViewModel : ViewModel() {
    private val _themeMode = mutableStateOf(ThemeMode.DEFAULT)
    val themeMode: State<ThemeMode> = _themeMode

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }
}
