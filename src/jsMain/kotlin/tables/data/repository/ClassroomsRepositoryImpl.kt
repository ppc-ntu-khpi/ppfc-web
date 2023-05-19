package tables.data.repository

import androidx.paging.PagingState
import api.ApiException
import app.cash.paging.PagingSource
import core.data.mapper.toDomain
import tables.data.dao.ClassroomsDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Classroom
import tables.domain.repository.ClassroomsRepository

class ClassroomsRepositoryImpl(
    private val classroomsDao: ClassroomsDao
) : ClassroomsRepository {

    override suspend fun saveClassroom(classroom: Classroom) {
        classroomsDao.saveClassroom(classroomRequest = classroom.toRequest())
    }

    override fun getClassroomsPagingSource(
        searchQuery: String
    ) = object : PagingSource<Long, Classroom>() {

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
                return LoadResult.Error(e.toDomain())
            }

            return LoadResult.Page(
                data = result.map { it.toDomain() },
                prevKey = null,
                nextKey = if (result.isEmpty()) null else offset + limit,
            )
        }
    }
}