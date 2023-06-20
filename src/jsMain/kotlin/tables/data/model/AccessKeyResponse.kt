/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessKeyResponse(
    val key: String,
    val expiresAt: Long
)