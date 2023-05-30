/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Course
import tables.domain.model.Group
import tables.domain.model.Id

interface GroupsRepository {
    suspend fun saveGroup(group: Group)

    suspend fun deleteGroups(ids: Set<Id>)

    fun getGroupsPagingSource(
        pageSize: Long,
        searchQuery: String?,
        course: Course?
    ): PagingSource<Long, Group>
}