package com.example.evspot.ui.theme

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
import com.example.evspot.ui.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = VoltGreen,
    secondary = SecondaryGray,
    tertiary = Pink80,
    background = DarkNavy,
    surface = DarkNavy,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = VoltGreen,
    secondary = SecondaryGray,
    tertiary = Pink40,
    background = BackgroundGray,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = SecondaryGray,
    onBackground = DarkNavy,
    onSurface = DarkNavy
)

@Composable
fun EVSpotTheme(
    themeMode: ThemeMode = ThemeMode.DEFAULT,
    darkTheme: Boolean = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.DEFAULT -> isSystemInDarkTheme()
    },
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
