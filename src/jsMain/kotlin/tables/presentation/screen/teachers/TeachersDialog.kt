/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

import tables.domain.model.Teacher

sealed interface TeachersDialog {
    class ManageTeacher(val teacher: Teacher?) : TeachersDialog
    class ConfirmDeletion(val itemsNumber: Long) : TeachersDialog
}