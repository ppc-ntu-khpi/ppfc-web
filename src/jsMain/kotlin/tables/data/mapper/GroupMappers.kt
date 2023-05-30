/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.GroupRequest
import tables.data.model.GroupResponse
import tables.domain.model.Group
import tables.domain.model.Id

fun Group.toRequest() = GroupRequest(
    number = number,
    courseId = course.number
)

fun GroupResponse.toDomain() = Group(
    id = Id.Value(value = id),
    number = number,
    course = course.toDomain()
)