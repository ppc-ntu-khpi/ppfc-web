/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.*

interface ScheduleRepository {
    suspend fun saveScheduleItem(scheduleItem: ScheduleItem)

    suspend fun deleteScheduleItems(ids: Set<Id>)

    suspend fun deleteAllScheduleItems()

    fun getSchedulePagingSource(
        pageSize: Long,
        dayNumber: DayNumber?,
        weekAlternation: WeekAlternation?,
        group: Group?,
        teacher: Teacher?
    ): PagingSource<Long, ScheduleItem>
}