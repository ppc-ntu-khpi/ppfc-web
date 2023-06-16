/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import infrastructure.extensions.dateFromString
import infrastructure.extensions.toISO8601String
import tables.data.model.ChangeRequest
import tables.data.model.ChangeResponse
import tables.domain.model.*
import kotlin.js.Date

fun Change.toRequest() = ChangeRequest(
    groupsIds = groups.map { it.id.value }.toSet(),
    classroomId = if(classroom == Classroom.Empty) null else classroom.id.value,
    teacherId = if(teacher == Teacher.Empty) null else teacher.id.value,
    subjectId = if(subject == Subject.Empty) null else subject.id.value,
    eventName = eventName,
    lessonNumber = lessonNumber?.toNumber(),
    dayNumber = dayNumber.toNumber() ,
    date = date.toISO8601String(),
    isNumerator = weekAlternation.isNumerator
)

fun ChangeResponse.toDomain() = Change(
    id = Id.Value(value = id),
    groups = groups.map { it.toDomain() }.toSet(),
    classroom = classroom?.toDomain() ?: Classroom.Empty,
    teacher = teacher?.toDomain() ?: Teacher.Empty,
    subject = subject?.toDomain() ?: Subject.Empty,
    eventName = eventName,
    lessonNumber = lessonNumber?.toLessonNumber(),
    dayNumber = dayNumber.toDayNumber(),
    date = dateFromString(date) ?: Date(),
    weekAlternation = isNumerator.toWeekAlternation()
)