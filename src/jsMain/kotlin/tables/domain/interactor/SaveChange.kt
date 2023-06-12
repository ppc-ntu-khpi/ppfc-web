/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Change
import tables.domain.model.Subject
import tables.domain.repository.ChangesRepository

class SaveChange(
    private val changesRepository: ChangesRepository
) : Interactor<SaveChange.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        var changeToSave = params.change.copy(
            eventName = params.change.eventName.takeUnless { it.isNullOrBlank() }
        )

        changeToSave = when {
            changeToSave.eventName != null -> changeToSave.copy(isSubject = false)
            changeToSave.subject != Subject.Empty -> changeToSave.copy(isSubject = true)
            else -> throw FormIsNotValidException()
        }

        changesRepository.saveChange(
            change = changeToSave
        )
    }

    data class Params(val change: Change)
}