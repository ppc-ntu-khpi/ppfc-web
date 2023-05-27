/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Discipline
import tables.domain.repository.DisciplinesRepository

class SaveDiscipline(
    private val disciplinesRepository: DisciplinesRepository
) : Interactor<SaveDiscipline.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        disciplinesRepository.saveDiscipline(discipline = params.discipline)
    }

    data class Params(val discipline: Discipline)
}