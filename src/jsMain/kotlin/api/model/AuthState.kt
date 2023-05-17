/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package api.model

import kotlinx.serialization.Serializable

@Serializable
enum class AuthState {
    LOGGED_IN,
    LOGGED_OUT,
    NEW_PASSWORD_REQUIRED
}