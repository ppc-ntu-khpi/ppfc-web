/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package app.main

import core.domain.model.Preferences
import coreui.util.UiEvent

data class MainViewState(
    val preferences: Preferences = Preferences.Empty,
    val event: UiEvent<MainViewEvent>? = null
) {
    companion object {
        val Empty = MainViewState()
    }
}