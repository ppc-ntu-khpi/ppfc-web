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
        searchQuery: String
    ) = object : PagingSource<Long, Classroom>() {

        init {
            classroomsPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Classroom>) = null

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Classroom> {
            val limit = params.loadSize.toLong()
            val offset = params.key ?: 0L

            val result = try {
                classroomsDao.getClassrooms(
                    limit = limit,
                    offset = offset,
                    searchQuery = searchQuery
                )
            } catch (e: ApiException) {
                return LoadResult.Error(e)
            }

            return LoadResult.Page(
                data = result.map { it.toDomain() },
                prevKey = if (offset == 0L) null else offset - limit,
                nextKey = if (result.isEmpty()) null else offset + limit,
            )
        }
    }
}