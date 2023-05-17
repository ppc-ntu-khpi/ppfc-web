/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain.observer

import core.domain.SubjectInteractor
import core.domain.model.AuthState
import core.domain.repository.AuthStateRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthState(
    private val authStateRepository: AuthStateRepository
) : SubjectInteractor<Unit, AuthState>() {

    override fun createObservable(params: Unit): Flow<AuthState> {
        return authStateRepository.observeAuthState()
    }
}