/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms.model

import coreui.model.TextFieldState
import tables.domain.model.Id

data class ClassroomState(
    val id: Id = Id.Empty,
    val name: TextFieldState = TextFieldState.Empty
) {
    companion object {
        val Empty = ClassroomState()
    }
}
