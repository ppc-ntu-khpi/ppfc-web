/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Classroom
import tables.domain.repository.ClassroomsRepository

class SaveClassroom(
    private val classroomsRepository: ClassroomsRepository
) : Interactor<SaveClassroom.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        classroomsRepository.saveClassroom(classroom = params.classroom)
    }

    data class Params(val classroom: Classroom)
}