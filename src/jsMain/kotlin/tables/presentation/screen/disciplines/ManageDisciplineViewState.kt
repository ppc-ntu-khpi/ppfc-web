/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

import tables.presentation.screen.disciplines.model.DisciplineState

data class ManageDisciplineViewState(
    val disciplineState: DisciplineState = DisciplineState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = ManageDisciplineViewState()
    }
}