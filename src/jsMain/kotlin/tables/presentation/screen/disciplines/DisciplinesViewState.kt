/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

import coreui.model.TextFieldState
import coreui.util.UiEvent
import tables.domain.model.Id

data class DisciplinesViewState(
    val searchQuery: TextFieldState = TextFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: DisciplinesDialog? = null,
    val event: UiEvent<DisciplinesViewEvent>? = null
) {
    companion object {
        val Empty = DisciplinesViewState()
    }
}