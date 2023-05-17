package tables.presentation.screen.classrooms

import coreui.util.UiMessage

sealed interface ClassroomsViewEvent {
    class Message(val message: UiMessage) : ClassroomsViewEvent
}