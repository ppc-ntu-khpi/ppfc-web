/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import tables.domain.model.ScheduleItem
import tables.domain.model.Subject

class ProcessAndValidateScheduleItem : Interactor<ProcessAndValidateScheduleItem.Params, ScheduleItem>() {

    override suspend fun doWork(params: Params): ScheduleItem {
        val scheduleItem = params.scheduleItem.copy(
            eventName = params.scheduleItem.eventName.takeUnless { it.isNullOrBlank() }
        )

        return when {
            scheduleItem.eventName != null -> scheduleItem.copy(isSubject = false)
            scheduleItem.subject != Subject.Empty -> scheduleItem.copy(isSubject = true)
            else -> throw FormIsNotValidException()
        }
    }

    data class Params(val scheduleItem: ScheduleItem)
}