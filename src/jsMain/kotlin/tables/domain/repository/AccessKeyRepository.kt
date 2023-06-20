/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import tables.domain.model.AccessKey

interface AccessKeyRepository {
    suspend fun generateAccessKey(): AccessKey
}