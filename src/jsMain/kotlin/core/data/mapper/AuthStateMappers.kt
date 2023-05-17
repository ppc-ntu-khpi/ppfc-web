package core.data.mapper

import api.model.AuthState
import api.model.AuthState.*

fun AuthState.toDomain(): core.domain.model.AuthState {
    return when(this) {
        LOGGED_IN -> core.domain.model.AuthState.LOGGED_IN
        LOGGED_OUT -> core.domain.model.AuthState.LOGGED_OUT
        NEW_PASSWORD_REQUIRED -> core.domain.model.AuthState.NEW_PASSWORD_REQUIRED
    }
}