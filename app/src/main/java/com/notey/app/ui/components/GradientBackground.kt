package com.notey.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.notey.app.ui.theme.GradientColors

/**
 * A gradient background for the app.
 * Creates a beautiful gradient based on the current theme.
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientColors.darkGradient,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(colors = gradientColors)
            )
    ) {
        content()
    }
}
