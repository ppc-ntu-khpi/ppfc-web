/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.presentation.screen.changepassword

import core.domain.NetworkException
import core.domain.TimeoutException
import coreui.extensions.onError
import coreui.extensions.withErrorMapper
import coreui.model.TextFieldState
import coreui.theme.AppTheme
import coreui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import onboarding.domain.interactor.ChallengeFailedException
import onboarding.domain.interactor.LogOut
import onboarding.domain.interactor.PassNewPasswordRequiredChallenge
import onboarding.domain.interactor.PasswordIsNotValidException

class ChangePasswordViewModel(
    private val passNewPasswordRequiredChallenge: PassNewPasswordRequiredChallenge,
    private val logOut: LogOut
) {

    private val loadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<ChangePasswordViewEvent>()

    private val _password = MutableStateFlow(TextFieldState.Empty)

    val state: StateFlow<ChangePasswordViewState> = combine(
        _password,
        loadingState.observable,
        uiEventManager.event
    ) { password, isLoading, event ->
        val isPasswordBlank = password.text.isBlank()

        ChangePasswordViewState(
            password = password,
            isFormBlank = isPasswordBlank,
            isLoading = isLoading,
            event = event
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ChangePasswordViewState.Empty,
    )

    fun setPassword(password: String) {
        _password.update {
            it.copy(
                text = password,
                error = null
            )
        }
    }

    fun changePassword() = launchWithLoader(loadingState) {
        val password = _password.value.text.trim()

        passNewPasswordRequiredChallenge(
            params = PassNewPasswordRequiredChallenge.Params(password = password)
        ).onError { cause ->
            when (cause) {
                is PasswordIsNotValidException -> {
                    _password.update {
                        it.copy(
                            error = AppTheme.stringResources.passwordIsInvalid
                        )
                    }
                }
            }
        }.withErrorMapper(
            errorMapper = { cause ->
                when (cause) {
                    is NetworkException -> AppTheme.stringResources.networkException
                    is TimeoutException -> AppTheme.stringResources.timeoutException
                    is ChallengeFailedException -> AppTheme.stringResources.newPasswordRequiredChallengeFailed
                    is PasswordIsNotValidException -> null
                    else -> AppTheme.stringResources.unexpectedErrorException
                }
            }
        ) { message ->
            sendEvent(
                event = ChangePasswordViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun navigateToLoginScreen() {
        logOut(Unit).launchIn(CoroutineScope(Dispatchers.Default))
    }

    private fun sendEvent(event: ChangePasswordViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun clearEvent(id: Long) {
        uiEventManager.clearEvent(id = id)
    }
}