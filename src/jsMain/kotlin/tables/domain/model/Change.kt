/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

import kotlin.js.Date

data class Change(
    val id: Id = Id.Empty,
    val group: Group = Group.Empty,
    val classroom: Classroom = Classroom.Empty,
    val teacher: Teacher = Teacher.Empty,
    val subject: Subject = Subject.Empty,
    val eventName: String? = null,
    val isSubject: Boolean = false,
    val lessonNumber: LessonNumber = LessonNumber.N0,
    val dayNumber: DayNumber = DayNumber.N1,
    val date: Date = Date(),
    val weekAlternation: WeekAlternation = WeekAlternation.NUMERATOR
) {
    companion object {
        val Empty = Change()
    }
}