/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

import coreui.model.NumberFieldState
import coreui.util.UiEvent
import tables.domain.model.Id

data class CoursesViewState(
    val searchQuery: NumberFieldState = NumberFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: CoursesDialog? = null,
    val event: UiEvent<CoursesViewEvent>? = null
) {
    companion object {
        val Empty = CoursesViewState()
    }
}