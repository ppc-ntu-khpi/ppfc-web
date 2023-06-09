/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import coreui.util.UiEvent
import tables.domain.model.Group
import tables.domain.model.Id
import tables.domain.model.Teacher
import tables.presentation.common.model.DayNumberOption
import tables.presentation.common.model.WeekAlternationOption
import tables.presentation.compose.PagingDropDownMenuState

data class ScheduleViewState(
    val rowsSelection: Map<Id, Boolean> = emptyMap(),
    val filterGroup: PagingDropDownMenuState<Group> = PagingDropDownMenuState.Empty(),
    val filterTeacher: PagingDropDownMenuState<Teacher> = PagingDropDownMenuState.Empty(),
    val filterDayNumber: DayNumberOption = DayNumberOption.ALL,
    val filterWeekAlternation: WeekAlternationOption = WeekAlternationOption.ALL,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val dialog: ScheduleDialog? = null,
    val event: UiEvent<ScheduleViewEvent>? = null
) {
    companion object {
        val Empty = ScheduleViewState()
    }
}