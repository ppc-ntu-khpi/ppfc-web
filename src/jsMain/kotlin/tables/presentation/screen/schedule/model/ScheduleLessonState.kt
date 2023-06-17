/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule.model

import coreui.model.TextFieldState
import tables.domain.model.Classroom
import tables.domain.model.Subject
import tables.domain.model.Teacher
import tables.domain.model.WeekAlternation
import tables.presentation.compose.PagingDropDownMenuState

data class ScheduleLessonState(
    val classroomsMenu: PagingDropDownMenuState<Classroom> = PagingDropDownMenuState.Empty(),
    val teachersMenu: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val subjectsMenu: PagingDropDownMenuState<Subject> = PagingDropDownMenuState.Empty(),
    val eventName: TextFieldState = TextFieldState.Empty,
    val lessonNumber: ScheduleLessonNumberOption = ScheduleLessonNumberOption.N1,
    val weekAlternation: WeekAlternation = WeekAlternation.NUMERATOR
) {
    companion object {
        val Empty = ScheduleLessonState()
    }
}
