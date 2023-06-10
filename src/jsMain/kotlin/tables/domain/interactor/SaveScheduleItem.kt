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
        scheduleRepository.saveScheduleItem(
            scheduleItem = params.scheduleItem.let { scheduleItem ->
                params.scheduleItem.copy(eventName = scheduleItem.eventName.takeUnless { it.isNullOrBlank() })
            }.let { scheduleItem ->
                if (scheduleItem.subject == Subject.Empty && scheduleItem.eventName == null) {
                    throw FormIsNotValidException()
                }
                scheduleItem
            }
        )
    }

    data class Params(val scheduleItem: ScheduleItem)
}