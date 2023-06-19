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

class SaveScheduleItems(
    private val scheduleRepository: ScheduleRepository
) : Interactor<SaveScheduleItems.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val scheduleItemsToSave = params.scheduleItems.map { scheduleItem ->
            val scheduleItemToSave = scheduleItem.copy(
                eventName = scheduleItem.eventName.takeUnless { it.isNullOrBlank() }
            )

            when {
                scheduleItemToSave.eventName != null -> scheduleItemToSave.copy(isSubject = false)
                scheduleItemToSave.subject != Subject.Empty -> scheduleItemToSave.copy(isSubject = true)
                else -> throw FormIsNotValidException()
            }
        }

        scheduleRepository.saveScheduleItems(scheduleItems = scheduleItemsToSave)
    }

    data class Params(val scheduleItems: List<ScheduleItem>)
}