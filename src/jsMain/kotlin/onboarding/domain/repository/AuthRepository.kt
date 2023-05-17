package onboarding.domain.repository

import onboarding.domain.model.AuthCredentials

interface AuthRepository {
    suspend fun logIn(credentials: AuthCredentials)
    suspend fun logOut()
    suspend fun passNewPasswordRequiredChallenge(password: String)
}