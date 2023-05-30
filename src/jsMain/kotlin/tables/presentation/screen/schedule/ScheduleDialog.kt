/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import tables.domain.model.ScheduleItem

sealed interface ScheduleDialog {
    class ManageScheduleItem(val scheduleItem: ScheduleItem?) : ScheduleDialog
    class ConfirmDeletion(val itemsNumber: Long) : ScheduleDialog
}