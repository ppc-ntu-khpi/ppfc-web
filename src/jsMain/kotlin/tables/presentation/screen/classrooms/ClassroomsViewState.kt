/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import coreui.model.TextFieldState
import coreui.util.UiEvent
import tables.domain.model.Id

data class ClassroomsViewState(
    val searchQuery: TextFieldState = TextFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: ClassroomsDialog? = null,
    val event: UiEvent<ClassroomsViewEvent>? = null
) {
    companion object {
        val Empty = ClassroomsViewState()
    }
}