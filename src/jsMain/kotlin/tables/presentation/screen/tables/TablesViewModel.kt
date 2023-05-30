/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

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

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val state: StateFlow<MainViewState> = combine(
        observePreferences.flow
    ) { preferences ->
        MainViewState(
            preferences = preferences.first()
        )
    }.stateIn(
        scope = coroutineScope,
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
        ).launchIn(coroutineScope)
    }

    fun logOut() {
        logOut(Unit).launchIn(coroutineScope)
    }
}