/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.ScheduleItem
import tables.domain.repository.ScheduleRepository

class SaveScheduleItems(
    private val scheduleRepository: ScheduleRepository,
    private val processAndValidateScheduleItem: ProcessAndValidateScheduleItem
) : Interactor<SaveScheduleItems.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        val scheduleItems = params.scheduleItems.map { scheduleItem ->
            processAndValidateScheduleItem.executeSync(
                params = ProcessAndValidateScheduleItem.Params(scheduleItem = scheduleItem)
            )
        }

        scheduleRepository.saveScheduleItems(scheduleItems = scheduleItems)
    }

    data class Params(val scheduleItems: List<ScheduleItem>)
}