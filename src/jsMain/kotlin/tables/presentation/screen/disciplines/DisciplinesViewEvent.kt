/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

import coreui.util.UiMessage

sealed interface DisciplinesViewEvent {
    class Message(val message: UiMessage) : DisciplinesViewEvent
    object DisciplineSaved : DisciplinesViewEvent
    object DisciplineDeleted : DisciplinesViewEvent
}