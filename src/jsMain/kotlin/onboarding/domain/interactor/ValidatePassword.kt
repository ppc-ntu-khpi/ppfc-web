/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ValidatePassword : Interactor<ValidatePassword.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val regex = Regex("^(?=.*[A-Z])(?=.*\\d).{8,}$")
        if(!regex.matches(params.password)) {
            throw PasswordIsNotValidException()
        }
    }

    data class Params(val password: String)
}