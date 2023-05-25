/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Classroom
import tables.domain.model.Id

interface ClassroomsRepository {
    suspend fun saveClassroom(classroom: Classroom)

    suspend fun deleteClassrooms(ids: Set<Id>)

    fun getClassroomsPagingSource(
        pageSize: Long,
        searchQuery: String
    ): PagingSource<Long, Classroom>
}