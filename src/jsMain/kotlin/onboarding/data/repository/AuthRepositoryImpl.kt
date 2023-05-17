package onboarding.data.repository

import api.ApiException
import onboarding.domain.model.AuthCredentials
import onboarding.data.dao.AuthDao
import core.data.mapper.rethrowDomain
import onboarding.data.mapper.toDto
import onboarding.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDao: AuthDao
) : AuthRepository {

    override suspend fun logIn(credentials: AuthCredentials) {
        try {
            authDao.logIn(credentials = credentials.toDto())
        } catch (e: ApiException) {
            e.rethrowDomain()
        }
    }

    override suspend fun logOut() {
        authDao.logOut()
    }

    override suspend fun passNewPasswordRequiredChallenge(password: String) {
        try {
            authDao.passNewPasswordRequiredChallenge(password = password)
        } catch (e: ApiException) {
            e.rethrowDomain()
        }
    }
}