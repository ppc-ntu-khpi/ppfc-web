/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

import kotlin.js.Date

data class AccessKey(
    val key: String = "",
    val expiresAt: Date = Date()
) {
    companion object {
        val Empty = AccessKey()
    }
}