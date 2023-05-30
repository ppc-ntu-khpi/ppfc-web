/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.ScheduleRequest
import tables.data.model.ScheduleResponse

class ScheduleDaoImpl(
    private val apiClient: ApiClient
) : ScheduleDao {

    override suspend fun saveScheduleItem(scheduleRequest: ScheduleRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(scheduleRequest)
        }
    }

    override suspend fun updateSchedule(scheduleRequest: ScheduleRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(scheduleRequest)
        }
    }

    override suspend fun deleteScheduleItems(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getScheduleItems(
        limit: Long,
        offset: Long,
        dayNumber: Long?,
        isNumerator: Boolean?,
        groupId: Long?,
        teacherId: Long?
    ): List<ScheduleResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (dayNumber != null) parameter("dayNumber", dayNumber)
            if (isNumerator != null) parameter("isNumerator", dayNumber)
            if (groupId != null) parameter("groupId", groupId)
            if (teacherId != null) parameter("teacherId", teacherId)
        }.body()
    }

    private companion object {
        const val PATH = "schedule"
    }
}