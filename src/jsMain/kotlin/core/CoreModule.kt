/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core

import core.data.dao.AuthStateDao
import core.data.dao.AuthStateDaoImpl
import core.data.dao.PreferencesDao
import core.data.dao.PreferencesDaoImpl
import core.data.repository.AuthStateRepositoryImpl
import core.data.repository.PreferencesRepositoryImpl
import core.domain.interactor.SaveColorScheme
import core.domain.observer.ObserveAuthState
import core.domain.observer.ObservePreferences
import core.domain.repository.AuthStateRepository
import core.domain.repository.PreferencesRepository
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val coreModule = module {
    /** Preferences */
    single<PreferencesDao> {
        PreferencesDaoImpl(
            dataStore = get(parameters = { parametersOf("PREFERENCES") })
        )
    }

    single<PreferencesRepository> {
        PreferencesRepositoryImpl(get())
    }

    single<ObservePreferences> {
        ObservePreferences(get())
    }

    single<SaveColorScheme> {
        SaveColorScheme(get())
    }

    /** Auth */
    single<AuthStateDao> {
        AuthStateDaoImpl(get())
    }

    single<AuthStateRepository> {
        AuthStateRepositoryImpl(get())
    }

    single {
        ObserveAuthState(get())
    }
}