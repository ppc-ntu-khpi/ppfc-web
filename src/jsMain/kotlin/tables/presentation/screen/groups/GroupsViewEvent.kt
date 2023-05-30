/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import coreui.util.UiMessage

sealed interface GroupsViewEvent {
    class Message(val message: UiMessage) : GroupsViewEvent
    object GroupSaved : GroupsViewEvent
    object GroupDeleted : GroupsViewEvent
}