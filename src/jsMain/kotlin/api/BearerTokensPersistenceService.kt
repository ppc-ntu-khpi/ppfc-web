/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

import api.model.BearerTokens
import kotlinx.coroutines.flow.Flow

interface BearerTokensPersistenceService {
    fun observeBearerTokens(): Flow<BearerTokens?>
    fun saveBearerTokens(bearerTokens: BearerTokens)
    fun getBearerTokens(): BearerTokens?
    fun clear()
}