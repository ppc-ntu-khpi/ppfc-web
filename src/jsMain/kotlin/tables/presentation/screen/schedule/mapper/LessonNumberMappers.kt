/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule.mapper

import tables.domain.model.LessonNumber
import tables.presentation.screen.schedule.model.ScheduleLessonNumberOption

fun ScheduleLessonNumberOption.toDomain(): LessonNumber = when (this) {
    ScheduleLessonNumberOption.N1 -> LessonNumber.N1
    ScheduleLessonNumberOption.N2 -> LessonNumber.N2
    ScheduleLessonNumberOption.N3 -> LessonNumber.N3
    ScheduleLessonNumberOption.N4 -> LessonNumber.N4
    ScheduleLessonNumberOption.N5 -> LessonNumber.N5
}

fun LessonNumber.toScheduleState(): ScheduleLessonNumberOption = when (this) {
    LessonNumber.N0 -> ScheduleLessonNumberOption.N1
    LessonNumber.N1 -> ScheduleLessonNumberOption.N1
    LessonNumber.N2 -> ScheduleLessonNumberOption.N2
    LessonNumber.N3 -> ScheduleLessonNumberOption.N3
    LessonNumber.N4 -> ScheduleLessonNumberOption.N4
    LessonNumber.N5 -> ScheduleLessonNumberOption.N5
}