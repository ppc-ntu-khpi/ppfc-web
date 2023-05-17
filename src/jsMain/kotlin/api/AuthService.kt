/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

import api.model.AuthCredentials
import api.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    fun observeAuthState(): Flow<AuthState>
    suspend fun logIn(credentials: AuthCredentials)
    fun logOut()
    suspend fun passNewPasswordRequiredChallenge(password: String)
}