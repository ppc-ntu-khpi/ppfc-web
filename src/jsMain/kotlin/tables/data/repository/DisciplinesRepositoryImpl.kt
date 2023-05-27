/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.DisciplinesDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.domain.repository.DisciplinesRepository

class DisciplinesRepositoryImpl(
    private val disciplinesDao: DisciplinesDao
) : DisciplinesRepository {

    private var disciplinesPagingSource: PagingSource<Long, Discipline>? = null

    override suspend fun saveDiscipline(discipline: Discipline) {
        if (discipline.id is Id.Value) {
            disciplinesDao.updateDiscipline(disciplineRequest = discipline.toRequest(), id = discipline.id.value)
        } else {
            disciplinesDao.saveDiscipline(disciplineRequest = discipline.toRequest())
        }
        disciplinesPagingSource?.invalidate()
    }

    override suspend fun deleteDisciplines(ids: Set<Id>) {
        disciplinesDao.deleteDisciplines(ids = ids.map { it.value }.toSet())
        disciplinesPagingSource?.invalidate()
    }

    override fun getDisciplinesPagingSource(
        pageSize: Long,
        searchQuery: String
    ) = object : PagingSource<Long, Discipline>() {

        init {
            disciplinesPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Discipline>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Discipline> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                disciplinesDao.getDisciplines(
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