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
    val group: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val selectedGroups: Set<Group> = emptySet(),
    val classroom: PagingDropDownMenuState<Classroom> = PagingDropDownMenuState.Empty(),
    val teacher: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val subject: PagingDropDownMenuState<Subject> = PagingDropDownMenuState.Empty(),
    val eventName: TextFieldState = TextFieldState.Empty,
    val lessonNumber: ChangeLessonNumberOption = ChangeLessonNumberOption.NOTHING
) {
    companion object {
        val Empty = ChangeLessonState()
    }
}
