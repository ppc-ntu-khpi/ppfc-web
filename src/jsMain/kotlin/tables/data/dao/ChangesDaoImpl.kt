/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.ChangeRequest
import tables.data.model.ChangeResponse

class ChangesDaoImpl(
    private val apiClient: ApiClient
) : ChangesDao {

    override suspend fun saveChange(changeRequest: ChangeRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(changeRequest)
        }
    }

    override suspend fun updateChange(changeRequest: ChangeRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(changeRequest)
        }
    }

    override suspend fun deleteChanges(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getChanges(
        limit: Long,
        offset: Long,
        date: String?,
        isNumerator: Boolean?,
        groupId: Long?,
        teacherId: Long?
    ): List<ChangeResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (date != null) parameter("date", date)
            if (isNumerator != null) parameter("isNumerator", isNumerator)
            if (groupId != null) parameter("groupId", groupId)
            if (teacherId != null) parameter("teacherId", teacherId)
        }.body()
    }

    private companion object {
        const val PATH = "change"
    }
}