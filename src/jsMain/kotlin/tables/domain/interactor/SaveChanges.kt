/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Change
import tables.domain.repository.ChangesRepository

class SaveChanges(
    private val changesRepository: ChangesRepository,
    private val processAndValidateChange: ProcessAndValidateChange
) : Interactor<SaveChanges.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val changesToSave = params.changes.map { change ->
            processAndValidateChange.executeSync(
                params = ProcessAndValidateChange.Params(change = change)
            )
        }

        changesRepository.saveChanges(changes = changesToSave)
    }

    data class Params(val changes: List<Change>)
}