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

class SaveChanges(
    private val changesRepository: ChangesRepository
) : Interactor<SaveChanges.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val changesToSave = params.changes.map { change ->
            val changeToSave = change.copy(
                eventName = change.eventName.takeUnless { it.isNullOrBlank() }
            )

            when {
                changeToSave.eventName != null -> changeToSave.copy(isSubject = false)
                changeToSave.subject != Subject.Empty -> changeToSave.copy(isSubject = true)
                else -> throw FormIsNotValidException()
            }
        }

        changesRepository.saveChanges(changes = changesToSave)
    }

    data class Params(val changes: List<Change>)
}