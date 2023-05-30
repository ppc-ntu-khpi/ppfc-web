/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

import tables.presentation.screen.teachers.model.TeacherState

data class ManageTeacherViewState(
    val teacherState: TeacherState = TeacherState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = ManageTeacherViewState()
    }
}