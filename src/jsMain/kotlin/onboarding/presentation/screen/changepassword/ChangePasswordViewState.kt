package onboarding.presentation.screen.changepassword

import coreui.model.TextFieldState
import coreui.util.UiEvent

data class ChangePasswordViewState(
    val password: TextFieldState = TextFieldState.Empty,
    val isFormBlank: Boolean = true,
    val isLoading: Boolean = false,
    val event: UiEvent<ChangePasswordViewEvent>? = null
) {
    companion object {
        val Empty = ChangePasswordViewState()
    }
}