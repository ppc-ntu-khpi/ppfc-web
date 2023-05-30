/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.ClassroomsDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Classroom
import tables.domain.model.Id
import tables.domain.repository.ClassroomsRepository

class ClassroomsRepositoryImpl(
    private val classroomsDao: ClassroomsDao
) : ClassroomsRepository {

    private var classroomsPagingSource: PagingSource<Long, Classroom>? = null

    override suspend fun saveClassroom(classroom: Classroom) {
        if (classroom.id is Id.Value) {
            classroomsDao.updateClassroom(classroomRequest = classroom.toRequest(), id = classroom.id.value)
        } else {
            classroomsDao.saveClassroom(classroomRequest = classroom.toRequest())
        }
        classroomsPagingSource?.invalidate()
    }

    override suspend fun deleteClassrooms(ids: Set<Id>) {
        classroomsDao.deleteClassrooms(ids = ids.map { it.value }.toSet())
        classroomsPagingSource?.invalidate()
    }

    override fun getClassroomsPagingSource(
        pageSize: Long,
        searchQuery: String?
    ) = object : PagingSource<Long, Classroom>() {

        init {
            classroomsPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Classroom>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Classroom> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                classroomsDao.getClassrooms(
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