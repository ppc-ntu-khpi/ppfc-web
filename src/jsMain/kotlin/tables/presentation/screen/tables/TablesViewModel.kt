package tables.presentation.screen.tables

import app.main.MainViewState
import core.domain.interactor.SaveColorScheme
import core.domain.model.ColorSchemeMode
import core.domain.observer.ObservePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import onboarding.domain.interactor.LogOut

class TablesViewModel(
    observePreferences: ObservePreferences,
    private val saveColorScheme: SaveColorScheme,
    private val logOut: LogOut
) {

    val state: StateFlow<MainViewState> = combine(
        observePreferences.flow
    ) { preferences ->
        MainViewState(
            preferences = preferences.first()
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = MainViewState.Empty,
    )

    init {
        observePreferences(Unit)
    }

    fun setColorSchemeMode(colorSchemeMode: ColorSchemeMode) {
        saveColorScheme(
            params = SaveColorScheme.Params(
                colorSchemeMode = colorSchemeMode
            )
        ).launchIn(CoroutineScope(Dispatchers.Default))
    }

    fun logOut() {
        logOut(Unit).launchIn(CoroutineScope(Dispatchers.Default))
    }
}