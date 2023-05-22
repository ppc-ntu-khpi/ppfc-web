/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Id
import tables.domain.repository.ClassroomsRepository

class DeleteClassrooms(
    private val classroomsRepository: ClassroomsRepository
) : Interactor<DeleteClassrooms.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        classroomsRepository.deleteClassrooms(ids = params.ids)
    }

    data class Params(val ids: Set<Id>)
}