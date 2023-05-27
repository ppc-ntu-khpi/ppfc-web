/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Id
import tables.domain.model.Subject

interface SubjectsRepository {
    suspend fun saveSubject(subject: Subject)

    suspend fun deleteSubjects(ids: Set<Id>)

    fun getSubjectsPagingSource(
        pageSize: Long,
        searchQuery: String
    ): PagingSource<Long, Subject>
}