/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import tables.domain.model.ScheduleItem

sealed interface ScheduleDialog {
    object CreateScheduleItems : ScheduleDialog
    class EditScheduleItem(val scheduleItem: ScheduleItem) : ScheduleDialog
    class ConfirmDeletion(val itemsNumber: Long) : ScheduleDialog
    object ConfirmDeletionOfAll : ScheduleDialog
}