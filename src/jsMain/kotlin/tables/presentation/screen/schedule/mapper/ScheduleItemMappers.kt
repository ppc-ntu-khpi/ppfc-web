/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule.mapper

import coreui.model.TextFieldState
import tables.domain.model.*
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.schedule.model.ScheduleItemState
import tables.presentation.screen.schedule.model.ScheduleLessonState

fun ScheduleLessonState.toDomain(
    group: Group,
    dayNumber: DayNumber
) = ScheduleItem(
    group = group,
    classroom = classroomsMenu.selectedItem ?: Classroom.Empty,
    teacher = teachersMenu.selectedItem ?: Teacher.Empty,
    subject = subjectsMenu.selectedItem ?: Subject.Empty,
    eventName = eventName.text,
    lessonNumber = lessonNumber.toDomain(),
    dayNumber = dayNumber,
    weekAlternation = weekAlternation
)

fun ScheduleItemState.toDomain() = ScheduleItem(
    id = id,
    group = groupsMenu.selectedItem ?: Group.Empty,
    classroom = classroomsMenu.selectedItem ?: Classroom.Empty,
    teacher = teachersMenu.selectedItem ?: Teacher.Empty,
    subject = subjectsMenu.selectedItem ?: Subject.Empty,
    eventName = eventName.text,
    lessonNumber = lessonNumber.toDomain(),
    dayNumber = dayNumber,
    weekAlternation = weekAlternation
)

fun ScheduleItem.toState() = ScheduleItemState(
    id = id,
    groupsMenu = PagingDropDownMenuState.Empty<Group>()
        .copy(selectedItem = group.takeIf { it != Group.Empty }),
    classroomsMenu = PagingDropDownMenuState.Empty<Classroom>()
        .copy(selectedItem = classroom.takeIf { it != Classroom.Empty }),
    teachersMenu = PagingDropDownMenuState.Empty<Teacher>()
        .copy(selectedItem = teacher.takeIf { it != Teacher.Empty }),
    subjectsMenu = PagingDropDownMenuState.Empty<Subject>()
        .copy(selectedItem = subject.takeIf { it != Subject.Empty }),
    eventName = TextFieldState.Empty.copy(text = eventName ?: ""),
    lessonNumber = lessonNumber.toScheduleState(),
    dayNumber = dayNumber,
    weekAlternation = weekAlternation
)