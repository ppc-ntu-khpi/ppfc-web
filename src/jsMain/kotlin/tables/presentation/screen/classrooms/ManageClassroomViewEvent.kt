/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import coreui.util.UiMessage

sealed interface ManageClassroomViewEvent {
    class Message(val message: UiMessage) : ManageClassroomViewEvent
    object ClassroomSaved : ManageClassroomViewEvent
}