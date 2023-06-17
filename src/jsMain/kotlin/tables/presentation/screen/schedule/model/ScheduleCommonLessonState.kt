/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule.model

import tables.domain.model.DayNumber
import tables.domain.model.Group
import tables.presentation.compose.PagingDropDownMenuState

data class ScheduleCommonLessonState(
    val groupsMenu: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val dayNumber: DayNumber = DayNumber.N1
) {
    companion object {
        val Empty = ScheduleCommonLessonState()
    }
}
