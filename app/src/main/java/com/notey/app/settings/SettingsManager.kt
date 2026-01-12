package com.notey.app.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.notey.app.ui.theme.ThemeMode

/**
 * Manages app settings including theme preference.
 */
class SettingsManager(context: Context) {
    
    private val prefs = context.getSharedPreferences("notey_settings", Context.MODE_PRIVATE)
    
    var themeMode: ThemeMode by mutableStateOf(loadTheme())
        private set
    
    private fun loadTheme(): ThemeMode {
        val themeName = prefs.getString("theme_mode", ThemeMode.DARK.name) ?: ThemeMode.DARK.name
        return try {
            ThemeMode.valueOf(themeName)
        } catch (e: Exception) {
            ThemeMode.DARK
        }
    }
    
    fun setTheme(mode: ThemeMode) {
        themeMode = mode
        prefs.edit().putString("theme_mode", mode.name).apply()
    }
}
