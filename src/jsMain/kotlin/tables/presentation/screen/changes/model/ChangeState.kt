/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes.model

import coreui.model.TextFieldState
import tables.domain.model.*
import tables.presentation.common.mapper.toDayNumber
import tables.presentation.compose.PagingDropDownMenuState
import kotlin.js.Date

data class ChangeState(
    val id: Id = Id.Empty,
    val group: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val selectedGroups: Set<Group> = emptySet(),
    val classroom: PagingDropDownMenuState<Classroom> = PagingDropDownMenuState.Empty(),
    val teacher: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val subject: PagingDropDownMenuState<Subject> = PagingDropDownMenuState.Empty(),
    val eventName: TextFieldState = TextFieldState.Empty,
    val lessonNumber: ChangeLessonNumberOption = ChangeLessonNumberOption.NOTHING,
    val dayNumber: DayNumber = Date().toDayNumber(),
    val date: Date = Date(),
    val weekAlternation: WeekAlternation = WeekAlternation.NUMERATOR
) {
    companion object {
        val Empty = ChangeState()
    }
}
