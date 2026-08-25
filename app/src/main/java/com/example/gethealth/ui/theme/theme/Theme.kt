package com.example.gethealth.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = ForestGreen,
    onPrimary = CardSurface,
    secondary = ForestGreenLight,
    background = WarmBackground,
    onBackground = ForestGreenDark,
    surface = CardSurface,
    onSurface = ForestGreenDark,
    surfaceVariant = WarmBackground,
    onSurfaceVariant = MutedText
)

private val DarkColors = darkColorScheme(
    primary = ForestGreenLight,
    secondary = BadgeGreenFg,
    background = ForestGreenDark,
    surface = Color(0xFF16352A)
)

/**
 * The app-wide Material 3 theme, styled to match the GetHealth design deck:
 * deep forest green as the primary/brand color, a warm off-white background,
 * and white cards. Every screen picks this up automatically through
 * MaterialTheme.colorScheme.* rather than hardcoding colors.
 */
@Composable
fun GetHealthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
