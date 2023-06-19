/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import tables.domain.model.Change
import tables.domain.model.Subject

class ProcessAndValidateChange : Interactor<ProcessAndValidateChange.Params, Change>() {

    override suspend fun doWork(params: Params): Change {
        val change = params.change.copy(
            eventName = params.change.eventName.takeUnless { it.isNullOrBlank() }
        )

        return when {
            change.eventName != null -> change.copy(isSubject = false)
            change.subject != Subject.Empty -> change.copy(isSubject = true)
            else -> throw FormIsNotValidException()
        }
    }

    data class Params(val change: Change)
}