/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.CourseRequest
import tables.data.model.CourseResponse

class CoursesDaoImpl(
    private val apiClient: ApiClient
) : CoursesDao {

    override suspend fun saveCourse(courseRequest: CourseRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(courseRequest)
        }
    }

    override suspend fun updateCourse(courseRequest: CourseRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(courseRequest)
        }
    }

    override suspend fun deleteCourse(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getCourses(
        limit: Long,
        offset: Long,
        searchQuery: String?
    ): List<CourseResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (searchQuery != null) parameter("query", searchQuery)
        }.body()
    }

    private companion object {
        const val PATH = "course"
    }
}