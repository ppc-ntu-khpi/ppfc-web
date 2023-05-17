/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.domain.model.ColorSchemeMode
import core.domain.model.Locale

private var appColor by mutableStateOf(ColorSchemeMode.LIGHT)
private var appStringResources by mutableStateOf(Locale.UA)

object AppTheme {
    val colors: ColorScheme
        get() = getColorScheme(appColor)
    val stringResources: StringResources
        get() = getStringResources(appStringResources)
}

@Composable
fun AppTheme(
    locale: Locale = Locale.UA,
    colorSchemeMode: ColorSchemeMode = ColorSchemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    appColor = colorSchemeMode
    appStringResources = locale

    content()
}