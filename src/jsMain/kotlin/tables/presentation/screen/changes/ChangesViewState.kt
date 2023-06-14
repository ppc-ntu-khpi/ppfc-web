/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import coreui.util.UiEvent
import tables.domain.model.Group
import tables.domain.model.Id
import tables.presentation.compose.PagingDropDownMenuState
import kotlin.js.Date

data class ChangesViewState(
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val filterGroup: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val filterDate: Date = Date(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: ChangesDialog? = null,
    val event: UiEvent<ChangesViewEvent>? = null
) {
    companion object {
        val Empty = ChangesViewState()
    }
}