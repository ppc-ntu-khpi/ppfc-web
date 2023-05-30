/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.GroupRequest
import tables.data.model.GroupResponse

interface GroupsDao {
    suspend fun saveGroup(groupRequest: GroupRequest)

    suspend fun updateGroup(groupRequest: GroupRequest, id: Long)

    suspend fun deleteGroups(ids: Set<Long>)

    suspend fun getGroups(
        limit: Long,
        offset: Long,
        searchQuery: String?,
        courseId: Long?
    ): List<GroupResponse>
}