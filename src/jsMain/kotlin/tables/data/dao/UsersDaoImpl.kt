/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import tables.data.model.UserResponse

class UsersDaoImpl(
    private val apiClient: ApiClient
) : UsersDao {

    override suspend fun deleteUsers(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun getUsers(
        limit: Long,
        offset: Long,
        searchQuery: String?
    ): List<UserResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (searchQuery != null) parameter("query", searchQuery)
        }.body()
    }

    private companion object {
        const val PATH = "user"
    }
}