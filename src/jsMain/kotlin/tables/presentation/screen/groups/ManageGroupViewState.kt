/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import tables.presentation.screen.groups.model.GroupState

data class ManageGroupViewState(
    val groupState: GroupState = GroupState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = ManageGroupViewState()
    }
}