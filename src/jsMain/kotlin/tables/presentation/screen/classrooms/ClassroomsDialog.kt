/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import tables.domain.model.Classroom

sealed interface ClassroomsDialog {
    class ManageClassroom(val classroom: Classroom?) : ClassroomsDialog
    class ConfirmDeletion(val itemsNumber: Long) : ClassroomsDialog
}