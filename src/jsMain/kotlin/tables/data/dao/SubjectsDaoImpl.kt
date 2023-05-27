/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.SubjectRequest
import tables.data.model.SubjectResponse

class SubjectsDaoImpl(
    private val apiClient: ApiClient
) : SubjectsDao {

    override suspend fun saveSubject(subjectRequest: SubjectRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(subjectRequest)
        }
    }

    override suspend fun updateSubject(subjectRequest: SubjectRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(subjectRequest)
        }
    }

    override suspend fun deleteSubjects(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getSubjects(
        limit: Long,
        offset: Long,
        searchQuery: String
    ): List<SubjectResponse> {
        return apiClient.client.get(PATH) {
            if(limit > 0) {
                parameter("limit", limit)
            }
            if(offset > 0) {
                parameter("offset", offset)
            }
            if(searchQuery.isNotBlank()) {
                parameter("query", searchQuery)
            }
        }.body()
    }

    private companion object {
        const val PATH = "subject"
    }
}