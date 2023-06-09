/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import tables.presentation.screen.schedule.model.ScheduleItemState

data class EditScheduleItemViewState(
    val scheduleItemState: ScheduleItemState = ScheduleItemState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = EditScheduleItemViewState()
    }
}