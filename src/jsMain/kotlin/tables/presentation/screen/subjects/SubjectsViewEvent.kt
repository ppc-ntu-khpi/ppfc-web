/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects

import coreui.util.UiMessage

sealed interface SubjectsViewEvent {
    class Message(val message: UiMessage) : SubjectsViewEvent
    object SubjectSaved : SubjectsViewEvent
    object SubjectDeleted : SubjectsViewEvent
}