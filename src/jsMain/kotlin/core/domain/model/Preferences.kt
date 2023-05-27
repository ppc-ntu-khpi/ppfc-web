/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val colorSchemeMode: ColorSchemeMode = ColorSchemeMode.LIGHT,
    val locale: Locale = Locale.UA
) {
    companion object {
        val Empty = Preferences()
    }
}