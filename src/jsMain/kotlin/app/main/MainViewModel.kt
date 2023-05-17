package app.main

import app.navigation.Screen
import core.domain.model.AuthState
import core.domain.observer.ObserveAuthState
import core.domain.observer.ObservePreferences
import coreui.util.UiEvent
import coreui.util.UiEventManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MainViewModel(
    observeAuthState: ObserveAuthState,
    observePreferences: ObservePreferences
) {

    private val uiEventManager = UiEventManager<MainViewEvent>()

    val state: StateFlow<MainViewState> = combine(
        observePreferences.flow,
        uiEventManager.event
    ) { preferences, event ->
        MainViewState(
            preferences = preferences,
            event = event
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = MainViewState.Empty,
    )

    init {
        observeAuthState.flow.onEach { authState ->
            val screen = when (authState) {
                AuthState.LOGGED_IN -> Screen.Tables
                AuthState.LOGGED_OUT -> Screen.Login
                AuthState.NEW_PASSWORD_REQUIRED -> Screen.ChangePassword
            }

            uiEventManager.emitEvent(
                event = UiEvent(event = MainViewEvent.Navigate(screen = screen))
            )
        }.launchIn(CoroutineScope(Dispatchers.Default))

        observeAuthState(Unit)
        observePreferences(Unit)
    }

    fun clearEvent(id: Long) {
        uiEventManager.clearEvent(id = id)
    }
}