package com.example.kigaliride.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KigaliRideDarkColors = darkColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryGreen,
    background = DarkBackground,
    surface = CardBackground,
    onPrimary = DarkBackground,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White,
    error = DangerRed
)

@Composable
fun KigaliRideTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KigaliRideDarkColors,
        typography = Typography,
        content = content
    )
}
