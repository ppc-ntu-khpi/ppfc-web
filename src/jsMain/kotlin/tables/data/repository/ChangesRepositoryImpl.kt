/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import infrastructure.extensions.toISO8601String
import tables.data.dao.ChangesDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.*
import tables.domain.repository.ChangesRepository
import kotlin.js.Date

class ChangesRepositoryImpl(
    private val changesDao: ChangesDao
) : ChangesRepository {

    private var changesPagingSource: PagingSource<Long, Change>? = null

    override suspend fun saveChange(change: Change) {
        if (change.id is Id.Value) {
            changesDao.updateChange(changeRequest = change.toRequest(), id = change.id.value)
        } else {
            changesDao.saveChange(changeRequest = change.toRequest())
        }
        changesPagingSource?.invalidate()
    }

    override suspend fun deleteChanges(ids: Set<Id>) {
        changesDao.deleteChanges(ids = ids.map { it.value }.toSet())
        changesPagingSource?.invalidate()
    }

    override suspend fun deleteAllChanges() {
        changesDao.deleteAllChanges()
    }

    override fun getChangesPagingSource(
        pageSize: Long,
        date: Date?,
        weekAlternation: WeekAlternation?,
        group: Group?,
        teacher: Teacher?
    ) = object : PagingSource<Long, Change>() {

        init {
            changesPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Change>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Change> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                changesDao.getChanges(
                    limit = pageSize,
                    offset = offset,
                    date = date?.toISO8601String(),
                    isNumerator = weekAlternation?.isNumerator,
                    groupId = group?.id?.value,
                    teacherId = teacher?.id?.value
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

    override suspend fun exportChangesToDocument(date: Date): File {
        return changesDao.exportChangesToDocument(date = date.toISO8601String())
    }
}