/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.presentation.screen.login

import core.domain.NetworkException
import core.domain.TimeoutException
import coreui.extensions.withErrorMapper
import coreui.model.TextFieldState
import coreui.theme.AppTheme
import coreui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import onboarding.domain.interactor.AuthenticationException
import onboarding.domain.interactor.LogIn
import onboarding.domain.model.AuthCredentials

class LoginViewModel(
    private val logIn: LogIn
) {

    private val loadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<LoginViewEvent>()

    private val _username = MutableStateFlow(TextFieldState.Empty)
    private val _password = MutableStateFlow(TextFieldState.Empty)

    val state: StateFlow<LoginViewState> = combine(
        _username,
        _password,
        loadingState.observable,
        uiEventManager.event
    ) { username, password, isLoading, event ->
        val isUsernameBlank = username.text.isBlank()
        val isPasswordBlank = password.text.isBlank()

        LoginViewState(
            username = username,
            password = password,
            isFormBlank = isUsernameBlank || isPasswordBlank,
            isLoading = isLoading,
            event = event
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = LoginViewState.Empty,
    )

    fun setUsername(username: String) {
        _username.update {
            it.copy(
                text = username,
                error = null
            )
        }
    }

    fun setPassword(password: String) {
        _password.update {
            it.copy(
                text = password,
                error = null
            )
        }
    }

    fun logIn() = launchWithLoader(loadingState) {
        val credentials = AuthCredentials(
            username = _username.value.text.trim(),
            password = _password.value.text.trim()
        )

        logIn(
            params = LogIn.Params(
                credentials = credentials
            )
        ).withErrorMapper(
            errorMapper = { cause ->
                when (cause) {
                    is NetworkException -> AppTheme.stringResources.networkException
                    is TimeoutException -> AppTheme.stringResources.timeoutException
                    is AuthenticationException -> AppTheme.stringResources.authenticationException
                    else -> AppTheme.stringResources.unexpectedErrorException
                }
            }
        ) { message ->
            sendEvent(
                event = LoginViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: LoginViewEvent) {
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