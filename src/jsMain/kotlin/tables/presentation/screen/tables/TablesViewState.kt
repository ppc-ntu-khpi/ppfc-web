/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.tables

import core.domain.model.Preferences

data class TablesViewState(
    val preferences: Preferences = Preferences.Empty,
    val dialog: TablesDialog? = null
) {
    companion object {
        val Empty = TablesViewState()
    }
}