/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Course
import tables.domain.model.Id

interface CoursesRepository {
    suspend fun saveCourse(course: Course)

    suspend fun deleteCourses(ids: Set<Id>)

    fun getCoursesPagingSource(
        pageSize: Long,
        searchQuery: String
    ): PagingSource<Long, Course>
}