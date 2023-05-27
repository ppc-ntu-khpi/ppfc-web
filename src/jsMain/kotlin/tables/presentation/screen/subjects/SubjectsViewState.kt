/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects

import coreui.model.TextFieldState
import coreui.util.UiEvent
import tables.domain.model.Id

data class SubjectsViewState(
    val searchQuery: TextFieldState = TextFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: SubjectsDialog? = null,
    val event: UiEvent<SubjectsViewEvent>? = null
) {
    companion object {
        val Empty = SubjectsViewState()
    }
}