/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain.observer

import core.domain.SubjectInteractor
import core.domain.model.Preferences
import core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class ObservePreferences(
    private val preferencesRepository: PreferencesRepository
) : SubjectInteractor<Unit, Preferences>() {

    override fun createObservable(params: Unit): Flow<Preferences> {
        return preferencesRepository.observePreferences()
    }
}