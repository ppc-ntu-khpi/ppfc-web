/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.DisciplineRequest
import tables.data.model.DisciplineResponse

class DisciplinesDaoImpl(
    private val apiClient: ApiClient
) : DisciplinesDao {

    override suspend fun saveDiscipline(disciplineRequest: DisciplineRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(disciplineRequest)
        }
    }

    override suspend fun updateDiscipline(disciplineRequest: DisciplineRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(disciplineRequest)
        }
    }

    override suspend fun deleteDisciplines(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getDisciplines(
        limit: Long,
        offset: Long,
        searchQuery: String?
    ): List<DisciplineResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (searchQuery != null) parameter("query", searchQuery)
        }.body()
    }

    private companion object {
        const val PATH = "discipline"
    }
}