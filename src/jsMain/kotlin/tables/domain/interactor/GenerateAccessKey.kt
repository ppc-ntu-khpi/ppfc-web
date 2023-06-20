/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.AccessKey
import tables.domain.repository.AccessKeyRepository

class GenerateAccessKey(
    private val accessKeyRepository: AccessKeyRepository
) : Interactor<Unit, AccessKey>() {

    override suspend fun doWork(params: Unit): AccessKey = withContext(Dispatchers.Default) {
        return@withContext accessKeyRepository.generateAccessKey()
    }
}