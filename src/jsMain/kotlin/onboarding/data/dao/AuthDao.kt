/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.data.dao

import api.model.AuthCredentials

interface AuthDao {
    suspend fun logIn(credentials: AuthCredentials)
    suspend fun logOut()
    suspend fun passNewPasswordRequiredChallenge(password: String)
}