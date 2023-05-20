/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.domain.model

import api.model.AuthCredentials

data class AuthCredentials(
    val username: String = "",
    val password: String = ""
) {
    companion object {
        val Empty = AuthCredentials()
    }
}