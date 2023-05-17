/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)