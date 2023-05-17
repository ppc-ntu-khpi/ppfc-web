/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api.model

import kotlinx.serialization.Serializable

@Serializable
enum class AuthResponseStatus {
    SUCCESS,
    FAILURE,
    NEW_PASSWORD_REQUIRED
}