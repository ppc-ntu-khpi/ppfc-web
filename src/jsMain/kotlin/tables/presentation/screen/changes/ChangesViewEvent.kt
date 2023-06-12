/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import coreui.util.UiMessage

sealed interface ChangesViewEvent {
    class Message(val message: UiMessage) : ChangesViewEvent
    object ChangeSaved : ChangesViewEvent
    object ChangeDeleted : ChangesViewEvent
}