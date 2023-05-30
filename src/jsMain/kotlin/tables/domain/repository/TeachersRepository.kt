/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.repository

import app.cash.paging.PagingSource
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.domain.model.Teacher

interface TeachersRepository {
    suspend fun saveTeacher(teacher: Teacher)

    suspend fun deleteTeachers(ids: Set<Id>)

    fun getTeachersPagingSource(
        pageSize: Long,
        searchQuery: String?,
        discipline: Discipline?
    ): PagingSource<Long, Teacher>
}