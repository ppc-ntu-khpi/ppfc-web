/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.GroupRequest
import tables.data.model.GroupResponse

class GroupsDaoImpl(
    private val apiClient: ApiClient
) : GroupsDao {

    override suspend fun saveGroup(groupRequest: GroupRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(groupRequest)
        }
    }

    override suspend fun updateGroup(groupRequest: GroupRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(groupRequest)
        }
    }

    override suspend fun deleteGroups(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getGroups(
        limit: Long,
        offset: Long,
        searchQuery: String?,
        courseId: Long?
    ): List<GroupResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (searchQuery != null) parameter("query", searchQuery)
            if (courseId != null) parameter("courseId", courseId)
        }.body()
    }

    private companion object {
        const val PATH = "group"
    }
}