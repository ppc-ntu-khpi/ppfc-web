/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes.mapper

import coreui.theme.AppTheme
import tables.domain.model.LessonNumber
import tables.presentation.screen.changes.model.ChangeLessonNumberOption

fun ChangeLessonNumberOption.toDomain(): LessonNumber? = when (this) {
    ChangeLessonNumberOption.NOTHING -> null
    ChangeLessonNumberOption.N0 -> LessonNumber.N0
    ChangeLessonNumberOption.N1 -> LessonNumber.N1
    ChangeLessonNumberOption.N2 -> LessonNumber.N2
    ChangeLessonNumberOption.N3 -> LessonNumber.N3
    ChangeLessonNumberOption.N4 -> LessonNumber.N4
    ChangeLessonNumberOption.N5 -> LessonNumber.N5
}

fun LessonNumber?.toChangeState(): ChangeLessonNumberOption = when (this) {
    LessonNumber.N0 -> ChangeLessonNumberOption.N0
    LessonNumber.N1 -> ChangeLessonNumberOption.N1
    LessonNumber.N2 -> ChangeLessonNumberOption.N2
    LessonNumber.N3 -> ChangeLessonNumberOption.N3
    LessonNumber.N4 -> ChangeLessonNumberOption.N4
    LessonNumber.N5 -> ChangeLessonNumberOption.N5
    null -> ChangeLessonNumberOption.NOTHING
}

fun ChangeLessonNumberOption.toTextRepresentation() = when(this) {
    ChangeLessonNumberOption.NOTHING -> AppTheme.stringResources.dropDownMenuNotSelected
    else -> this.number.toString()
}