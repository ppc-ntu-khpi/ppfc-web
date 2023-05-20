/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import onboarding.domain.model.AuthCredentials
import onboarding.domain.repository.AuthRepository

class LogIn(
    private val authRepository: AuthRepository
) : Interactor<LogIn.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        authRepository.logIn(credentials = params.credentials)
    }

    data class Params(val credentials: AuthCredentials)
}