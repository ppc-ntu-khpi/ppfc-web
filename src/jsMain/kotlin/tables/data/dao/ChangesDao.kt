/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.ChangeRequest
import tables.data.model.ChangeResponse

interface ChangesDao {
    suspend fun saveChange(changeRequest: ChangeRequest)

    suspend fun updateChange(changeRequest: ChangeRequest, id: Long)

    suspend fun deleteChanges(ids: Set<Long>)

    suspend fun getChanges(
        limit: Long,
        offset: Long,
        date: String?,
        isNumerator: Boolean?,
        groupId: Long?,
        teacherId: Long?
    ): List<ChangeResponse>
}