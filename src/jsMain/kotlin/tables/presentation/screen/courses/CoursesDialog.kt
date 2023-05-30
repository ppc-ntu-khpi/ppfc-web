/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

import tables.domain.model.Course

sealed interface CoursesDialog {
    class ManageCourse(val course: Course?) : CoursesDialog
    class ConfirmDeletion(val itemsNumber: Long) : CoursesDialog
}