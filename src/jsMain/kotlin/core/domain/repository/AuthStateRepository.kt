/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain.repository

import core.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthStateRepository {
    fun observeAuthState(): Flow<AuthState>
}