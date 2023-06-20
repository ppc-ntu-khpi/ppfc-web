/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.AccessKeyResponse

interface AccessKeyDao {
    suspend fun generateKey(): AccessKeyResponse
}