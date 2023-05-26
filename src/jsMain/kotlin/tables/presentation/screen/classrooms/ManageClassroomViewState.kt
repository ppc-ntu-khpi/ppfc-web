/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import tables.presentation.screen.classrooms.model.ClassroomState

data class ManageClassroomViewState(
    val classroomState: ClassroomState = ClassroomState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = ManageClassroomViewState()
    }
}