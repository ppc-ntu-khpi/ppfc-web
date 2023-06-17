/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.ScheduleRequest
import tables.data.model.ScheduleResponse

interface ScheduleDao {
    suspend fun saveScheduleItem(scheduleRequest: ScheduleRequest)

    suspend fun updateSchedule(scheduleRequest: ScheduleRequest, id: Long)

    suspend fun deleteScheduleItems(ids: Set<Long>)

    suspend fun deleteAllScheduleItems()

    suspend fun getScheduleItems(
        limit: Long,
        offset: Long,
        dayNumber: Long?,
        isNumerator: Boolean?,
        groupId: Long?,
        teacherId: Long?
    ): List<ScheduleResponse>
}