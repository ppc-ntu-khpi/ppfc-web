/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import onboarding.domain.repository.AuthRepository

class LogOut(
    private val authRepository: AuthRepository
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit): Unit = withContext(Dispatchers.Default) {
        authRepository.logOut()
    }
}