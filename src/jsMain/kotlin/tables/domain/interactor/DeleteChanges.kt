/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Id
import tables.domain.repository.ChangesRepository

class DeleteChanges(
    private val changesRepository: ChangesRepository
) : Interactor<DeleteChanges.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        changesRepository.deleteChanges(ids = params.ids)
    }

    data class Params(val ids: Set<Id>)
}