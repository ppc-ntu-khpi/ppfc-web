/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.ChangeRequest
import tables.data.model.ChangeResponse
import tables.domain.model.Change
import tables.domain.model.Id
import tables.domain.model.Subject
import tables.domain.model.Teacher

fun Change.toRequest() = ChangeRequest(
    groupId = group.id.value,
    classroomId = classroom.id.value,
    teacherId = if(teacher == Teacher.Empty) null else teacher.id.value,
    subjectId = if(subject == Subject.Empty) null else subject.id.value,
    eventName = eventName,
    lessonNumber = lessonNumber.toNumber(),
    date = date ?: "",
    isNumerator = weekAlternation.isNumerator
)

fun ChangeResponse.toDomain() = Change(
    id = Id.Value(value = id),
    group = group.toDomain(),
    classroom = classroom.toDomain(),
    teacher = teacher?.toDomain() ?: Teacher.Empty,
    subject = subject?.toDomain() ?: Subject.Empty,
    eventName = eventName,
    lessonNumber = lessonNumber.toLessonNumber(),
    date = date,
    weekAlternation = isNumerator.toWeekAlternation()
)