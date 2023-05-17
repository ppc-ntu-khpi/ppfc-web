package onboarding.presentation.screen.changepassword

import coreui.util.UiMessage

sealed interface ChangePasswordViewEvent {
    class Message(val message: UiMessage) : ChangePasswordViewEvent
}