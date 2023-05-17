package core.data.repository

import core.data.dao.AuthStateDao
import core.data.mapper.toDomain
import core.domain.model.AuthState
import core.domain.repository.AuthStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthStateRepositoryImpl(
    private val authStateDao: AuthStateDao
) : AuthStateRepository {

    override fun observeAuthState(): Flow<AuthState> = authStateDao.observeAuthState().map { it.toDomain() }
}