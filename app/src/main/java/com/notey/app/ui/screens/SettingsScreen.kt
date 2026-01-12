package com.notey.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notey.app.ui.components.GlassCard
import com.notey.app.ui.components.GradientBackground
import com.notey.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    onBack: () -> Unit
) {
    val gradientColors = when (currentTheme) {
        ThemeMode.LIGHT -> GradientColors.lightGradient
        ThemeMode.DARK -> GradientColors.darkGradient
        ThemeMode.CYBERPUNK -> GradientColors.cyberpunkGradient
    }
    
    val textColor = when (currentTheme) {
        ThemeMode.LIGHT -> Color.Black
        ThemeMode.DARK -> Color.White
        ThemeMode.CYBERPUNK -> CyberpunkColors.OnBackground
    }
    
    GradientBackground(gradientColors = gradientColors) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Settings", color = textColor) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = textColor)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    "Theme",
                    color = textColor,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Theme Options
                ThemeOption(
                    name = "🌙 Dark",
                    isSelected = currentTheme == ThemeMode.DARK,
                    previewColors = listOf(Color(0xFF1A237E), Color(0xFF4A148C)),
                    textColor = textColor,
                    onClick = { onThemeChange(ThemeMode.DARK) }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ThemeOption(
                    name = "☀️ Light",
                    isSelected = currentTheme == ThemeMode.LIGHT,
                    previewColors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)),
                    textColor = textColor,
                    onClick = { onThemeChange(ThemeMode.LIGHT) }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ThemeOption(
                    name = "🌆 Cyberpunk",
                    isSelected = currentTheme == ThemeMode.CYBERPUNK,
                    previewColors = listOf(Color(0xFFFF00FF), Color(0xFF00FFFF)),
                    textColor = textColor,
                    onClick = { onThemeChange(ThemeMode.CYBERPUNK) }
                )
            }
        }
    }
}

@Composable
fun ThemeOption(
    name: String,
    isSelected: Boolean,
    previewColors: List<Color>,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) previewColors.first() else Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color preview circles
        Row {
            previewColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(name, color = textColor, fontSize = 16.sp)
        
        Spacer(modifier = Modifier.weight(1f))
        
        if (isSelected) {
            Text("✓", color = previewColors.first(), fontSize = 20.sp)
        }
    }
}
