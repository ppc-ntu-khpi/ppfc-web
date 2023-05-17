/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class Preferences(
    val colorSchemeMode: ColorSchemeMode = ColorSchemeMode.LIGHT,
    val locale: Locale = Locale.UA
) {
    companion object {
        val Empty = Preferences()
    }
}