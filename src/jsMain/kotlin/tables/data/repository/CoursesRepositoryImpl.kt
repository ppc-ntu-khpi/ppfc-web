/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.CoursesDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Course
import tables.domain.model.Id
import tables.domain.repository.CoursesRepository

class CoursesRepositoryImpl(
    private val coursesDao: CoursesDao
) : CoursesRepository {

    private var coursesPagingSource: PagingSource<Long, Course>? = null

    override suspend fun saveCourse(course: Course) {
        if (course.id is Id.Value) {
            coursesDao.updateCourse(courseRequest = course.toRequest(), id = course.id.value)
        } else {
            coursesDao.saveCourse(courseRequest = course.toRequest())
        }
        coursesPagingSource?.invalidate()
    }

    override suspend fun deleteCourses(ids: Set<Id>) {
        coursesDao.deleteCourse(ids = ids.map { it.value }.toSet())
        coursesPagingSource?.invalidate()
    }

    override fun getCoursesPagingSource(
        pageSize: Long,
        searchQuery: String
    ) = object : PagingSource<Long, Course>() {

        init {
            coursesPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Course>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Course> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                coursesDao.getCourses(
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