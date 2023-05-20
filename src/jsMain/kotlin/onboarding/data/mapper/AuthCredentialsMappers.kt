/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package onboarding.data.mapper

import onboarding.domain.model.AuthCredentials

fun AuthCredentials.toDto() = api.model.AuthCredentials(
    username = username,
    password = password
)