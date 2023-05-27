/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.CourseRequest
import tables.data.model.CourseResponse
import tables.domain.model.Course
import tables.domain.model.Id

fun Course.toRequest() = CourseRequest(
    number = number
)

fun CourseResponse.toDomain() = Course(
    id = Id.Value(value = id),
    number = number
)