/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule.model

import coreui.model.TextFieldState
import tables.domain.model.*
import tables.presentation.compose.PagingDropDownMenuState

data class ScheduleItemState(
    val id: Id = Id.Empty,
    val group: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val classroom: PagingDropDownMenuState<Classroom> = PagingDropDownMenuState.Empty(),
    val teacher: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val subject: PagingDropDownMenuState<Subject> = PagingDropDownMenuState.Empty(),
    val eventName: TextFieldState = TextFieldState.Empty,
    val isSubject: Boolean = false,
    val lessonNumber: LessonNumber = LessonNumber.N1,
    val dayNumber: DayNumber = DayNumber.N1,
    val weekAlternation: WeekAlternation = WeekAlternation.NUMERATOR
) {
    companion object {
        val Empty = ScheduleItemState()
    }
}
