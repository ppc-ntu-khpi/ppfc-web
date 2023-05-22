/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.ClassroomRequest
import tables.data.model.ClassroomResponse
import tables.domain.model.Classroom
import tables.domain.model.Id

fun Classroom.toRequest() = ClassroomRequest(
    name = name
)

fun ClassroomResponse.toDomain() = Classroom(
    id = Id.Value(value = id),
    name = name
)