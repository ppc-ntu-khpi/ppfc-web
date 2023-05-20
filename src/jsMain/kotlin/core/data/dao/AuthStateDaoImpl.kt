/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.data.dao

import api.AuthService
import api.model.AuthState
import kotlinx.coroutines.flow.Flow

class AuthStateDaoImpl(
    private val authService: AuthService
) : AuthStateDao {

    override fun observeAuthState(): Flow<AuthState> = authService.observeAuthState()
}