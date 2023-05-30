/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.TeachersDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.domain.model.Teacher
import tables.domain.repository.TeachersRepository

class TeachersRepositoryImpl(
    private val teachersDao: TeachersDao
) : TeachersRepository {

    private var teachersPagingSource: PagingSource<Long, Teacher>? = null

    override suspend fun saveTeacher(teacher: Teacher) {
        if (teacher.id is Id.Value) {
            teachersDao.updateTeacher(teacherRequest = teacher.toRequest(), id = teacher.id.value)
        } else {
            teachersDao.saveTeacher(teacherRequest = teacher.toRequest())
        }
        teachersPagingSource?.invalidate()
    }

    override suspend fun deleteTeachers(ids: Set<Id>) {
        teachersDao.deleteTeachers(ids = ids.map { it.value }.toSet())
        teachersPagingSource?.invalidate()
    }

    override fun getTeachersPagingSource(
        pageSize: Long,
        searchQuery: String?,
        discipline: Discipline?
    ) = object : PagingSource<Long, Teacher>() {

        init {
            teachersPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Teacher>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Teacher> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                teachersDao.getTeachers(
                    limit = pageSize,
                    offset = offset,
                    searchQuery = searchQuery,
                    disciplineId = discipline?.id?.value
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