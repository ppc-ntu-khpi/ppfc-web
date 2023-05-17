/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api

import api.model.BearerTokens
import core.infrastructure.DataStore
import infrastructure.getGeneric
import infrastructure.observeGeneric
import infrastructure.saveGeneric
import kotlinx.coroutines.flow.Flow

class BearerTokensPersistenceServiceImpl(
    private val dataStore: DataStore
) : BearerTokensPersistenceService {

    override fun observeBearerTokens(): Flow<BearerTokens?> = dataStore.observeGeneric(
        key = DS_BEARER_TOKENS_KEY
    )

    override fun saveBearerTokens(bearerTokens: BearerTokens) {
        dataStore.saveGeneric(
            key = DS_BEARER_TOKENS_KEY,
            value = bearerTokens
        )
    }

    override fun getBearerTokens(): BearerTokens? {
        return dataStore.getGeneric(
            key = DS_BEARER_TOKENS_KEY
        )
    }

    override fun clear() {
        dataStore.clear(key = DS_BEARER_TOKENS_KEY)
    }

    companion object {
        private const val DS_BEARER_TOKENS_KEY = "BEARER_TOKENS"
    }
}