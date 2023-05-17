/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val status: AuthResponseStatus = AuthResponseStatus.FAILURE,
    val accessToken: String = "",
    val refreshToken: String = "",
    val session: String = "",
    val error: String = ""
)