/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.TeacherRequest
import tables.data.model.TeacherResponse

interface TeachersDao {
    suspend fun saveTeacher(teacherRequest: TeacherRequest)

    suspend fun updateTeacher(teacherRequest: TeacherRequest, id: Long)

    suspend fun deleteTeachers(ids: Set<Long>)

    suspend fun getTeachers(
        limit: Long,
        offset: Long,
        searchQuery: String?,
        disciplineId: Long?
    ): List<TeacherResponse>
}