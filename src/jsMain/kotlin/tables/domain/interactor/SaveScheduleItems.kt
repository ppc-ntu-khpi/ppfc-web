/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import tables.domain.model.ScheduleItem

class SaveScheduleItems(
    private val saveScheduleItem: SaveScheduleItem
) : Interactor<SaveScheduleItems.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        params.scheduleItems.map { scheduleItem ->
            async {
                saveScheduleItem.executeSync(
                    params = SaveScheduleItem.Params(scheduleItem = scheduleItem)
                )
            }
        }.awaitAll()
    }

    data class Params(val scheduleItems: List<ScheduleItem>)
}