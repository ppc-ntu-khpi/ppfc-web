/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Id
import tables.domain.model.User

interface UsersRepository {
    suspend fun deleteUsers(ids: Set<Id>)

    fun getUsersPagingSource(
        pageSize: Long,
        searchQuery: String?
    ): PagingSource<Long, User>
}