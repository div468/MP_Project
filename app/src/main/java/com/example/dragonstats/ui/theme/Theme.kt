package com.example.dragonstats.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colores para modo oscuro
val GreenPrimary = Color(0xFF4CAF50)
val GreenDark = Color(0xFF2E7D32)
val BlackBackground = Color(0xFF000000)
val DarkGray = Color(0xFF1A1A1A)

// Colores para modo claro
val GreenPrimaryLight = Color(0xFF4CAF50)
val GreenLightVariant = Color(0xFF81C784)
val WhiteBackground = Color(0xFFFAFAFA)
val LightGray = Color(0xFFF5F5F5)
val DarkText = Color(0xFF212121)
val MediumGray = Color(0xFFE0E0E0)

// Esquema de colores oscuro
private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenDark,
    tertiary = GreenPrimary,
    background = BlackBackground,
    surface = DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFB0B0B0)
)

// Esquema de colores claro
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryLight,
    secondary = GreenLightVariant,
    tertiary = GreenDark,
    background = WhiteBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = DarkText,
    onTertiary = Color.White,
    onBackground = DarkText,
    onSurface = DarkText,
    surfaceVariant = LightGray,
    onSurfaceVariant = Color(0xFF666666)
)

@Composable
fun DragonStatsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) {
                GreenPrimary.toArgb()
            } else {
                GreenPrimaryLight.toArgb()
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}