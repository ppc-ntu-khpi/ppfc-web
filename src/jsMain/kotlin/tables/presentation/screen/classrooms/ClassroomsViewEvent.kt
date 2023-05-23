/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import coreui.util.UiMessage

sealed interface ClassroomsViewEvent {
    class Message(val message: UiMessage) : ClassroomsViewEvent
    object ClassroomDeleted : ClassroomsViewEvent
}