/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms.mapper

import coreui.model.TextFieldState
import tables.domain.model.Classroom
import tables.presentation.screen.classrooms.model.ClassroomState

fun ClassroomState.toDomain() = Classroom(
    id = id,
    name = name.text
)

fun Classroom.toState() = ClassroomState(
    id = id,
    name = TextFieldState.Empty.copy(text = name)
)