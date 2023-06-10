/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.ScheduleItem
import tables.domain.model.Subject
import tables.domain.repository.ScheduleRepository

class SaveScheduleItem(
    private val scheduleRepository: ScheduleRepository
) : Interactor<SaveScheduleItem.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        var scheduleItemToSave = params.scheduleItem.copy(
            eventName = params.scheduleItem.eventName.takeUnless { it.isNullOrBlank() }
        )

        scheduleItemToSave = when {
            scheduleItemToSave.eventName != null -> scheduleItemToSave.copy(isSubject = false)
            scheduleItemToSave.subject != Subject.Empty -> scheduleItemToSave.copy(isSubject = true)
            else -> throw FormIsNotValidException()
        }

        scheduleRepository.saveScheduleItem(
            scheduleItem = scheduleItemToSave
        )
    }

    data class Params(val scheduleItem: ScheduleItem)
}