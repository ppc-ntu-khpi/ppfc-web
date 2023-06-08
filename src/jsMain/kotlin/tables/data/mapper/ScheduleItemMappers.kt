/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.ScheduleRequest
import tables.data.model.ScheduleResponse
import tables.domain.model.Id
import tables.domain.model.ScheduleItem
import tables.domain.model.Subject

fun ScheduleItem.toRequest() = ScheduleRequest(
    groupId = group.id.value,
    classroomId = classroom.id.value,
    teacherId = teacher.id.value,
    subjectId = subject.id.value,
    eventName = eventName,
    lessonNumber = lessonNumber.toNumber(),
    dayNumber = dayNumber.toNumber(),
    isNumerator = weekAlternation.isNumerator
)

fun ScheduleResponse.toDomain() = ScheduleItem(
    id = Id.Value(value = id),
    group = group.toDomain(),
    classroom = classroom.toDomain(),
    teacher = teacher.toDomain(),
    subject = subject?.toDomain() ?: Subject.Empty,
    eventName = eventName,
    lessonNumber = lessonNumber.toLessonNumber(),
    dayNumber = dayNumber.toDayNumber(),
    weekAlternation = isNumerator.toWeekAlternation()
)