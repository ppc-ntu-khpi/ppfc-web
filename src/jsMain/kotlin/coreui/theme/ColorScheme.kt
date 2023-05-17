/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

import core.domain.model.ColorSchemeMode
import org.jetbrains.compose.web.css.CSSColorValue

val appLightScheme = ColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

val appDarkScheme = ColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

fun getColorScheme(colorSchemeMode: ColorSchemeMode): ColorScheme {
    return when (colorSchemeMode) {
        ColorSchemeMode.LIGHT -> appLightScheme
        ColorSchemeMode.DARK -> appDarkScheme
    }
}

data class ColorScheme(
    val primary: CSSColorValue,
    val onPrimary: CSSColorValue,
    val primaryContainer: CSSColorValue,
    val onPrimaryContainer: CSSColorValue,
    val inversePrimary: CSSColorValue,
    val secondary: CSSColorValue,
    val onSecondary: CSSColorValue,
    val secondaryContainer: CSSColorValue,
    val onSecondaryContainer: CSSColorValue,
    val tertiary: CSSColorValue,
    val onTertiary: CSSColorValue,
    val tertiaryContainer: CSSColorValue,
    val onTertiaryContainer: CSSColorValue,
    val background: CSSColorValue,
    val onBackground: CSSColorValue,
    val surface: CSSColorValue,
    val onSurface: CSSColorValue,
    val surfaceVariant: CSSColorValue,
    val onSurfaceVariant: CSSColorValue,
    val surfaceTint: CSSColorValue,
    val inverseSurface: CSSColorValue,
    val inverseOnSurface: CSSColorValue,
    val error: CSSColorValue,
    val onError: CSSColorValue,
    val errorContainer: CSSColorValue,
    val onErrorContainer: CSSColorValue,
    val outline: CSSColorValue,
    val outlineVariant: CSSColorValue,
    val scrim: CSSColorValue,
)