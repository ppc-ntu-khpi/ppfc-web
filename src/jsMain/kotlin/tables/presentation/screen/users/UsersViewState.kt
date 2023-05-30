/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.users

import coreui.model.TextFieldState
import coreui.util.UiEvent
import tables.domain.model.Id

data class UsersViewState(
    val searchQuery: TextFieldState = TextFieldState.Empty,
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: UsersDialog? = null,
    val event: UiEvent<UsersViewEvent>? = null
) {
    companion object {
        val Empty = UsersViewState()
    }
}