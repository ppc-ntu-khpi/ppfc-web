/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.UserResponse

interface UsersDao {
    suspend fun deleteUsers(ids: Set<Long>)

    suspend fun getUsers(
        limit: Long,
        offset: Long,
        searchQuery: String?
    ): List<UserResponse>
}