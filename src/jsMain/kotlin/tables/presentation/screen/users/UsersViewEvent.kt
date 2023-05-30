/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.users

import coreui.util.UiMessage

sealed interface UsersViewEvent {
    class Message(val message: UiMessage) : UsersViewEvent
    object UserDeleted : UsersViewEvent
}