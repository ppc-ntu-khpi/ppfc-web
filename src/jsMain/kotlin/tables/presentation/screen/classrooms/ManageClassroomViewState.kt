/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import coreui.util.UiEvent
import tables.presentation.screen.classrooms.model.ClassroomState

data class ManageClassroomViewState(
    val classroomState: ClassroomState = ClassroomState.Empty,
    val isFormBlank: Boolean = true,
    val isLoading: Boolean = false,
    val event: UiEvent<ManageClassroomViewEvent>? = null
) {
    companion object {
        val Empty = ManageClassroomViewState()
    }
}