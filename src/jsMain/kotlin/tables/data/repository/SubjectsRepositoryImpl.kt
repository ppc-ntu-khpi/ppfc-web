/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.SubjectsDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Id
import tables.domain.model.Subject
import tables.domain.repository.SubjectsRepository

class SubjectsRepositoryImpl(
    private val subjectsDao: SubjectsDao
) : SubjectsRepository {

    private var subjectsPagingSource: PagingSource<Long, Subject>? = null

    override suspend fun saveSubject(subject: Subject) {
        if (subject.id is Id.Value) {
            subjectsDao.updateSubject(subjectRequest = subject.toRequest(), id = subject.id.value)
        } else {
            subjectsDao.saveSubject(subjectRequest = subject.toRequest())
        }
        subjectsPagingSource?.invalidate()
    }

    override suspend fun deleteSubjects(ids: Set<Id>) {
        subjectsDao.deleteSubjects(ids = ids.map { it.value }.toSet())
        subjectsPagingSource?.invalidate()
    }

    override fun getSubjectsPagingSource(
        pageSize: Long,
        searchQuery: String
    ) = object : PagingSource<Long, Subject>() {

        init {
            subjectsPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Subject>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Subject> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                subjectsDao.getSubjects(
                    limit = pageSize,
                    offset = offset,
                    searchQuery = searchQuery
                )
            } catch (e: ApiException) {
                return LoadResult.Error(e)
            }

            return LoadResult.Page(
                data = result.map { it.toDomain() },
                prevKey = if (page <= 0) null else page - 1,
                nextKey = if (result.size < pageSize) null else page + 1,
                itemsBefore = offset.toInt()
            )
        }
    }
}