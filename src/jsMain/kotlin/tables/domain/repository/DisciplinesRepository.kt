/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Discipline
import tables.domain.model.Id

interface DisciplinesRepository {
    suspend fun saveDiscipline(discipline: Discipline)

    suspend fun deleteDisciplines(ids: Set<Id>)

    fun getDisciplinesPagingSource(
        pageSize: Long,
        searchQuery: String
    ): PagingSource<Long, Discipline>
}