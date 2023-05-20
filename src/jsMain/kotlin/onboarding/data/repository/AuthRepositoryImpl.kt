/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.data.repository

import onboarding.data.dao.AuthDao
import onboarding.data.mapper.toDto
import onboarding.domain.model.AuthCredentials
import onboarding.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDao: AuthDao
) : AuthRepository {

    override suspend fun logIn(credentials: AuthCredentials) {
        authDao.logIn(credentials = credentials.toDto())
    }

    override suspend fun logOut() {
        authDao.logOut()
    }

    override suspend fun passNewPasswordRequiredChallenge(password: String) {
        authDao.passNewPasswordRequiredChallenge(password = password)
    }
}