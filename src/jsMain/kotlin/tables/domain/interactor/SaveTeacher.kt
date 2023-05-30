/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Teacher
import tables.domain.repository.TeachersRepository

class SaveTeacher(
    private val teachersRepository: TeachersRepository
) : Interactor<SaveTeacher.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        teachersRepository.saveTeacher(teacher = params.teacher)
    }

    data class Params(val teacher: Teacher)
}