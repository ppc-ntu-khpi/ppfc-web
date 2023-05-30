/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import coreui.model.TextFieldState
import coreui.util.UiEvent
import tables.domain.model.Course
import tables.domain.model.Id
import tables.presentation.compose.PagingDropDownMenuState

data class GroupsViewState(
    val searchQuery: TextFieldState = TextFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val filterCourse: PagingDropDownMenuState<Course> = PagingDropDownMenuState(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: GroupsDialog? = null,
    val event: UiEvent<GroupsViewEvent>? = null
) {
    companion object {
        val Empty = GroupsViewState()
    }
}