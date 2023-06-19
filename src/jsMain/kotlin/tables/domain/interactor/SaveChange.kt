/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Change
import tables.domain.repository.ChangesRepository

class SaveChange(
    private val changesRepository: ChangesRepository,
    private val processAndValidateChange: ProcessAndValidateChange
) : Interactor<SaveChange.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val change = processAndValidateChange.executeSync(
            params = ProcessAndValidateChange.Params(change = params.change)
        )

        changesRepository.saveChange(change = change)
    }

    data class Params(val change: Change)
}