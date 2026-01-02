package com.franciscogarciagarzon.pricetracker.presentation.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue, // From Color.kt
    onPrimary = OnPrimaryBlue, // From Color.kt
    primaryContainer = Color(0xFFD8E2FF),
    onPrimaryContainer = Color(0xFF001A41),

    secondary = SecondaryGray, // From Color.kt
    onSecondary = OnSecondaryGray, // From Color.kt
    secondaryContainer = Color(0xFFD9E2F8),
    onSecondaryContainer = Color(0xFF101C3B),

    tertiary = TertiaryTeal, // From Color.kt
    onTertiary = OnTertiaryTeal, // From Color.kt
    tertiaryContainer = Color(0xFF75F9D5), // Success/Teal
    onTertiaryContainer = Color(0xFF002118),

    error = ErrorRed, // From Color.kt
    onError = OnErrorRed, // From Color.kt
    errorContainer = ErrorContainerRed, // Light Red/Pink (0xFFFFDAD6)
    onErrorContainer = Color(0xFF410002), // Correct text color for error container

    background = LightBackground, // From Color.kt
    onBackground = Color(0xFF1B1B1F),
    surface = LightSurface, // From Color.kt
    onSurface = OnLightSurface, // From Color.kt
    surfaceVariant = Color(0xFFE0E2EC),
    onSurfaceVariant = Color(0xFF44474F),
    outline = Color(0xFF74777F),
)

// This now contains the Dark Theme colors (dark containers, light text, dark background)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFAEC6FF),
    onPrimary = Color(0xFF002E68),
    primaryContainer = Color(0xFF004494),
    onPrimaryContainer = Color(0xFFD8E2FF),

    secondary = Color(0xFFBDC7E1),
    onSecondary = Color(0xFF25314D),
    secondaryContainer = Color(0xFF3B4865),
    onSecondaryContainer = Color(0xFFD9E2F8),

    tertiary = Color(0xFF55DCC0),
    onTertiary = Color(0xFF00372D),
    tertiaryContainer = Color(0xFF005142), // Dark Teal for success container
    onTertiaryContainer = Color(0xFF75F9D5),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A), // Dark Red for error container
    onErrorContainer = Color(0xFFFFDAD6), // The light text color on the dark container

    background = DarkBackground, // From Color.kt
    onBackground = Color(0xFFE4E2E6),
    surface = DarkSurface, // From Color.kt
    onSurface = OnDarkSurface, // From Color.kt
    surfaceVariant = Color(0xFF44474F),
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = Color(0xFF8E9099),
)


@Composable
fun PriceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
//    dynamicColor: Boolean = true,
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