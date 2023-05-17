package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.ClassroomRequest
import tables.data.model.ClassroomResponse

class ClassroomsDaoImpl(
    private val apiClient: ApiClient
) : ClassroomsDao {

    override suspend fun saveClassroom(classroomRequest: ClassroomRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(classroomRequest)
        }
    }

    override suspend fun getClassrooms(
        limit: Long,
        offset: Long,
        searchQuery: String
    ): List<ClassroomResponse> {
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
        const val PATH = "classroom"
    }
}