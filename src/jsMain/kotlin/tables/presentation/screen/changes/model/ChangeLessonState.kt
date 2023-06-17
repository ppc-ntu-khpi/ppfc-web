/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes.model

import coreui.model.TextFieldState
import tables.domain.model.Classroom
import tables.domain.model.Group
import tables.domain.model.Subject
import tables.domain.model.Teacher
import tables.presentation.compose.PagingDropDownMenuState

data class ChangeLessonState(
    val groupsMenu: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val selectedGroups: Set<Group> = emptySet(),
    val classroomsMenu: PagingDropDownMenuState<Classroom> = PagingDropDownMenuState.Empty(),
    val teachersMenu: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val subjectsMenu: PagingDropDownMenuState<Subject> = PagingDropDownMenuState.Empty(),
    val eventName: TextFieldState = TextFieldState.Empty,
    val lessonNumber: ChangeLessonNumberOption = ChangeLessonNumberOption.NOTHING
) {
    companion object {
        val Empty = ChangeLessonState()
    }
}
