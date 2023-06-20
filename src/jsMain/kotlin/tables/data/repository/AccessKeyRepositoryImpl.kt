/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import tables.data.dao.AccessKeyDao
import tables.data.mapper.toDomain
import tables.domain.model.AccessKey
import tables.domain.repository.AccessKeyRepository

class AccessKeyRepositoryImpl(
    private val accessKeyDao: AccessKeyDao
) : AccessKeyRepository {

    override suspend fun generateAccessKey(): AccessKey {
        return accessKeyDao.generateKey().toDomain()
    }
}