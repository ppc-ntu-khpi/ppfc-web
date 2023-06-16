/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import tables.presentation.screen.changes.model.ChangeState

data class EditChangeViewState(
    val changeState: ChangeState = ChangeState.Empty,
    val isFormBlank: Boolean = true,
    val canAddGroups: Boolean = false
) {
    companion object {
        val Empty = EditChangeViewState()
    }
}