package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CleanLightColorScheme = lightColorScheme(
    primary = MintGreenPrimary,
    onPrimary = MintGreenOnPrimary,
    primaryContainer = MintGreenPrimaryContainer,
    onPrimaryContainer = MintGreenOnPrimaryContainer,
    secondary = SageSecondary,
    onSecondary = SageOnSecondary,
    secondaryContainer = SageSecondaryContainer,
    onSecondaryContainer = SageOnSecondaryContainer,
    tertiary = Class1AssetAccent,
    onTertiary = MintGreenOnPrimary,
    tertiaryContainer = Class1AssetBg,
    onTertiaryContainer = Class1AssetAccent,
    background = CleanPaperBackground,
    onBackground = SoftCharcoalText,
    surface = CleanPaperSurface,
    onSurface = SoftCharcoalText,
    surfaceVariant = CleanPaperCard,
    onSurfaceVariant = SoftCharcoalTextSecondary,
    outline = CleanPaperBorder,
    outlineVariant = CleanPaperBorder.copy(alpha = 0.6f)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Pure light theme by default as requested
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = CleanLightColorScheme,
        typography = Typography,
        content = content
    )
}
