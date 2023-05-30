/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

import coreui.util.UiMessage

sealed interface TeachersViewEvent {
    class Message(val message: UiMessage) : TeachersViewEvent
    object TeacherSaved : TeachersViewEvent
    object TeacherDeleted : TeachersViewEvent
}