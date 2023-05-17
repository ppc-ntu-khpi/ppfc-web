/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api.mapper

import api.model.BearerTokens

fun BearerTokens.toKtorBearerTokens() =
    io.ktor.client.plugins.auth.providers.BearerTokens(
        accessToken = accessToken,
        refreshToken = refreshToken
    )