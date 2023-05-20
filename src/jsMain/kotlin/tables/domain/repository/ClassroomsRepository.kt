/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Classroom

interface ClassroomsRepository {
    suspend fun saveClassroom(classroom: Classroom)

    fun getClassroomsPagingSource(
        searchQuery: String
    ): PagingSource<Long, Classroom>
}