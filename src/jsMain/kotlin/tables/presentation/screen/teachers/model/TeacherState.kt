/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers.model

import coreui.model.TextFieldState
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.presentation.compose.PagingDropDownMenuState

data class TeacherState(
    val id: Id = Id.Empty,
    val firstName: TextFieldState = TextFieldState.Empty,
    val lastName: TextFieldState = TextFieldState.Empty,
    val middleName: TextFieldState = TextFieldState.Empty,
    val discipline: PagingDropDownMenuState<Discipline> = PagingDropDownMenuState.Empty(),
    val isHeadTeacher: Boolean = false
) {
    companion object {
        val Empty = TeacherState()
    }
}
