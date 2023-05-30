/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.TeacherRequest
import tables.data.model.TeacherResponse

class TeachersDaoImpl(
    private val apiClient: ApiClient
) : TeachersDao {

    override suspend fun saveTeacher(teacherRequest: TeacherRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(teacherRequest)
        }
    }

    override suspend fun updateTeacher(teacherRequest: TeacherRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(teacherRequest)
        }
    }

    override suspend fun deleteTeachers(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getTeachers(
        limit: Long,
        offset: Long,
        searchQuery: String?,
        disciplineId: Long?
    ): List<TeacherResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (searchQuery != null) parameter("query", searchQuery)
            if (disciplineId != null) parameter("disciplineId", disciplineId)
        }.body()
    }

    private companion object {
        const val PATH = "teacher"
    }
}