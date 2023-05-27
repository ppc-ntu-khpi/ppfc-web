/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Subject
import tables.domain.repository.SubjectsRepository

class SaveSubject(
    private val subjectsRepository: SubjectsRepository
) : Interactor<SaveSubject.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        subjectsRepository.saveSubject(subject = params.subject)
    }

    data class Params(val subject: Subject)
}