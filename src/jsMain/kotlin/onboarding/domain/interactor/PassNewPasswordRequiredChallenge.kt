/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.interactor

import core.domain.Interactor
import onboarding.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PassNewPasswordRequiredChallenge(
    private val validatePassword: ValidatePassword,
    private val authRepository: AuthRepository
) : Interactor<PassNewPasswordRequiredChallenge.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        validatePassword.executeSync(
            params = ValidatePassword.Params(password = params.password)
        )

        authRepository.passNewPasswordRequiredChallenge(password = params.password)
    }

    data class Params(val password: String)
}