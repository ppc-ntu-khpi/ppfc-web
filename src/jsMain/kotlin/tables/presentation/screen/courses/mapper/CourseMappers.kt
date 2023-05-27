/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses.mapper

import coreui.model.NumberFieldState
import tables.domain.model.Course
import tables.presentation.screen.courses.model.CourseState

fun CourseState.toDomain() = Course(
    id = id,
    number = number.number ?: 0L
)

fun Course.toState() = CourseState(
    id = id,
    number = NumberFieldState.Empty.copy(number = number)
)