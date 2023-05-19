package onboarding.data.repository

import api.ApiException
import core.data.mapper.toDomain
import onboarding.data.dao.AuthDao
import onboarding.data.mapper.toDto
import onboarding.domain.model.AuthCredentials
import onboarding.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDao: AuthDao
) : AuthRepository {

    override suspend fun logIn(credentials: AuthCredentials) {
        try {
            authDao.logIn(credentials = credentials.toDto())
        } catch (e: ApiException) {
            throw e.toDomain()
        }
    }

    override suspend fun logOut() {
        authDao.logOut()
    }

    override suspend fun passNewPasswordRequiredChallenge(password: String) {
        try {
            authDao.passNewPasswordRequiredChallenge(password = password)
        } catch (e: ApiException) {
            throw e.toDomain()
        }
    }
}