/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.*
import kotlin.js.Date

interface ChangesRepository {
    suspend fun saveChange(change: Change)

    suspend fun saveChanges(changes: List<Change>)

    suspend fun deleteChanges(ids: Set<Id>)

    suspend fun deleteAllChanges()

    fun getChangesPagingSource(
        pageSize: Long,
        date: Date?,
        weekAlternation: WeekAlternation?,
        group: Group?,
        teacher: Teacher?
    ): PagingSource<Long, Change>

    suspend fun exportChangesToDocument(date: Date): File
}