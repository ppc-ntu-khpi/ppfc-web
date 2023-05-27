/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

import tables.presentation.screen.courses.model.CourseState

data class ManageCourseViewState(
    val courseState: CourseState = CourseState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = ManageCourseViewState()
    }
}