/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import tables.presentation.screen.classrooms.model.ClassroomState

sealed interface ClassroomsDialog {
    class ManageClassroom(val classroomState: ClassroomState?) : ClassroomsDialog
    class ConfirmDeletion(val itemsNumber: Long) : ClassroomsDialog
}