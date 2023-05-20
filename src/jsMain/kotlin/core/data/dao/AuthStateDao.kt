/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.data.dao

import api.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthStateDao {
    fun observeAuthState(): Flow<AuthState>
}