/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

import coreui.model.TextFieldState
import coreui.util.UiEvent
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.presentation.compose.PagingDropDownMenuState

data class TeachersViewState(
    val searchQuery: TextFieldState = TextFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val filterDiscipline: PagingDropDownMenuState<Discipline> = PagingDropDownMenuState.Empty(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: TeachersDialog? = null,
    val event: UiEvent<TeachersViewEvent>? = null
) {
    companion object {
        val Empty = TeachersViewState()
    }
}