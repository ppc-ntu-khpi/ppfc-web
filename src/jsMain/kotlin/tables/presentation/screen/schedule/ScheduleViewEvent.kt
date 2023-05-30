/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import coreui.util.UiMessage

sealed interface ScheduleViewEvent {
    class Message(val message: UiMessage) : ScheduleViewEvent
    object ScheduleItemSaved : ScheduleViewEvent
    object ScheduleItemDeleted : ScheduleViewEvent
}