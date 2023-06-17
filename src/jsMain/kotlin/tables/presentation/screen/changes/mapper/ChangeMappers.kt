/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes.mapper

import coreui.model.TextFieldState
import tables.domain.model.*
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.changes.model.ChangeLessonState
import tables.presentation.screen.changes.model.ChangeState
import kotlin.js.Date

fun ChangeLessonState.toDomain(
    date: Date,
    dayNumber: DayNumber,
    weekAlternation: WeekAlternation
) = Change(
    groups = selectedGroups,
    classroom = classroomsMenu.selectedItem ?: Classroom.Empty,
    teacher = teachersMenu.selectedItem ?: Teacher.Empty,
    subject = subjectsMenu.selectedItem ?: Subject.Empty,
    eventName = eventName.text,
    lessonNumber = lessonNumber.toDomain(),
    dayNumber = dayNumber,
    date = date,
    weekAlternation = weekAlternation
)

fun ChangeState.toDomain() = Change(
    id = id,
    groups = selectedGroups,
    classroom = classroomsMenu.selectedItem ?: Classroom.Empty,
    teacher = teachersMenu.selectedItem ?: Teacher.Empty,
    subject = subjectsMenu.selectedItem ?: Subject.Empty,
    eventName = eventName.text,
    lessonNumber = lessonNumber.toDomain(),
    date = date,
    dayNumber = dayNumber,
    weekAlternation = weekAlternation
)

fun Change.toState() = ChangeState(
    id = id,
    selectedGroups = groups,
    classroomsMenu = PagingDropDownMenuState.Empty<Classroom>()
        .copy(selectedItem = classroom.takeIf { it != Classroom.Empty }),
    teachersMenu = PagingDropDownMenuState.Empty<Teacher>()
        .copy(selectedItem = teacher.takeIf { it != Teacher.Empty }),
    subjectsMenu = PagingDropDownMenuState.Empty<Subject>()
        .copy(selectedItem = subject.takeIf { it != Subject.Empty }),
    eventName = TextFieldState.Empty.copy(text = eventName ?: ""),
    lessonNumber = lessonNumber.toChangeState(),
    date = date,
    dayNumber = dayNumber,
    weekAlternation = weekAlternation
)