/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers.mapper

import coreui.model.TextFieldState
import tables.domain.model.Discipline
import tables.domain.model.Teacher
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.teachers.model.TeacherState

fun TeacherState.toDomain() = Teacher(
    id = id,
    firstName = firstName.text,
    lastName = lastName.text,
    middleName = middleName.text,
    discipline = disciplinesMenu.selectedItem ?: Discipline.Empty,
    isHeadTeacher = isHeadTeacher
)

fun Teacher.toState() = TeacherState(
    id = id,
    firstName = TextFieldState.Empty.copy(text = firstName),
    lastName = TextFieldState.Empty.copy(text = lastName),
    middleName = TextFieldState.Empty.copy(text = middleName),
    disciplinesMenu = PagingDropDownMenuState.Empty<Discipline>()
        .copy(selectedItem = discipline.takeIf { it != Discipline.Empty }),
    isHeadTeacher = isHeadTeacher
)