/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import tables.presentation.screen.schedule.model.ScheduleItemState

data class CreateScheduleItemsViewState(
    val scheduleItemsStates: List<ScheduleItemState> = listOf(ScheduleItemState.Empty),
    val isFormBlank: Boolean = true,
    val canAddItems: Boolean = true,
    val canRemoveItems: Boolean = false
) {
    companion object {
        val Empty = CreateScheduleItemsViewState()
    }
}