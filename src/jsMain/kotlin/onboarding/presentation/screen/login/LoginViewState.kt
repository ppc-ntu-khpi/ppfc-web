package onboarding.presentation.screen.login

import coreui.model.TextFieldState
import coreui.util.UiEvent

data class LoginViewState(
    val username: TextFieldState = TextFieldState.Empty,
    val password: TextFieldState = TextFieldState.Empty,
    val isFormBlank: Boolean = true,
    val isLoading: Boolean = false,
    val event: UiEvent<LoginViewEvent>? = null
) {
    companion object {
        val Empty = LoginViewState()
    }
}