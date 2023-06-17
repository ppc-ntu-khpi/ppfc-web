/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule.model

import coreui.model.TextFieldState
import tables.domain.model.*
import tables.presentation.compose.PagingDropDownMenuState

data class ScheduleItemState(
    val id: Id = Id.Empty,
    val groupsMenu: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val classroomsMenu: PagingDropDownMenuState<Classroom> = PagingDropDownMenuState.Empty(),
    val teachersMenu: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val subjectsMenu: PagingDropDownMenuState<Subject> = PagingDropDownMenuState.Empty(),
    val eventName: TextFieldState = TextFieldState.Empty,
    val lessonNumber: ScheduleLessonNumberOption = ScheduleLessonNumberOption.N1,
    val dayNumber: DayNumber = DayNumber.N1,
    val weekAlternation: WeekAlternation = WeekAlternation.NUMERATOR
) {
    companion object {
        val Empty = ScheduleItemState()
    }
}
