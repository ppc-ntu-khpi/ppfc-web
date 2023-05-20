/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.data.dao

import api.AuthService
import api.model.AuthCredentials

class AuthDaoImpl(
    private val authService: AuthService
) : AuthDao {

    override suspend fun logIn(credentials: AuthCredentials) {
        authService.logIn(credentials = credentials)
    }

    override suspend fun logOut() {
        authService.logOut()
    }

    override suspend fun passNewPasswordRequiredChallenge(password: String) {
        authService.passNewPasswordRequiredChallenge(password = password)
    }
}