/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.*

interface ChangesRepository {
    suspend fun saveChange(change: Change)

    suspend fun deleteChanges(ids: Set<Id>)

    fun getChangesPagingSource(
        pageSize: Long,
        date: String?,
        weekAlternation: WeekAlternation?,
        group: Group?,
        teacher: Teacher?
    ): PagingSource<Long, Change>
}