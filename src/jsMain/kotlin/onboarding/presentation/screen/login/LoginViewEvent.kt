package onboarding.presentation.screen.login

import coreui.util.UiMessage

sealed interface LoginViewEvent {
    class Message(val message: UiMessage) : LoginViewEvent
}