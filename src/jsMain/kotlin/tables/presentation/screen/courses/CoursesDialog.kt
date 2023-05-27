/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

import tables.presentation.screen.courses.model.CourseState

sealed interface CoursesDialog {
    class ManageCourse(val courseState: CourseState?) : CoursesDialog
    class ConfirmDeletion(val itemsNumber: Long) : CoursesDialog
}