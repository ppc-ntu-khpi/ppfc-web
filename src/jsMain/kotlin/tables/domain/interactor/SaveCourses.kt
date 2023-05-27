/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Course
import tables.domain.repository.CoursesRepository

class SaveCourse(
    private val coursesRepository: CoursesRepository
) : Interactor<SaveCourse.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        coursesRepository.saveCourse(course = params.course)
    }

    data class Params(val course: Course)
}