package com.notey.app.ui.theme

import androidx.compose.ui.graphics.Color

// Theme Mode
enum class ThemeMode {
    LIGHT, DARK, CYBERPUNK
}

// Global Aliases for Backward Compatibility (and simpler default usage)
val AccentCyan = Color(0xFF00BCD4)
val AccentPink = Color(0xFFD81B60)
val GradientStart = Color(0xFF1A237E)
val GradientMiddle = Color(0xFF4A148C)
val GradientEnd = Color.Black
val GlassBackground = Color(0xFF1A1A2E)
val GlassWhite = Color(0x26FFFFFF)
val GlassBorder = Color(0x4DFFFFFF)
val TextPrimary = Color.White
val TextSecondary = Color(0xB3FFFFFF)

// --- LIGHT THEME ---
object LightColors {
    val Background = Color(0xFFF5F5F5)
    val Surface = Color(0xFFFFFFFF)
    val Primary = Color(0xFF1976D2)
    val OnPrimary = Color.White
    val OnBackground = Color(0xFF1C1B1F)
    val OnSurface = Color(0xFF1C1B1F)
    val GlassWhite = Color(0x40000000)
    val GlassBorder = Color(0x20000000)
}

// --- DARK THEME ---
object DarkColors {
    val Background = Color(0xFF0D0D1A)
    val Surface = Color(0xFF1A1A2E)
    val Primary = Color(0xFF00BCD4)
    val OnPrimary = Color.White
    val OnBackground = Color.White
    val OnSurface = Color.White
    val GlassWhite = Color(0x26FFFFFF)
    val GlassBorder = Color(0x4DFFFFFF)
}

// --- CYBERPUNK THEME ---
object CyberpunkColors {
    val Background = Color(0xFF0A0A0F)
    val Surface = Color(0xFF1A0A2E)
    val Primary = Color(0xFFFF00FF) // Magenta
    val Secondary = Color(0xFF00FFFF) // Cyan
    val OnPrimary = Color.Black
    val OnBackground = Color(0xFF00FFFF) // Cyan text
    val OnSurface = Color(0xFFFF00FF) // Magenta text
    val GlassWhite = Color(0x30FF00FF)
    val GlassBorder = Color(0x60FF00FF)
    val Accent = Color(0xFFFFFF00) // Yellow
}

// Gradient Colors for each theme
object GradientColors {
    val darkGradient = listOf(Color(0xFF1A237E), Color(0xFF4A148C), Color.Black)
    val lightGradient = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB), Color.White)
    val cyberpunkGradient = listOf(Color(0xFF0A0A0F), Color(0xFF1A0A2E), Color(0xFF000000))
}