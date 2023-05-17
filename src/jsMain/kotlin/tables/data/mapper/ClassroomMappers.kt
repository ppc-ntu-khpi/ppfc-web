package tables.data.mapper

import tables.data.model.ClassroomRequest
import tables.data.model.ClassroomResponse
import tables.domain.model.Classroom

fun Classroom.toRequest() = ClassroomRequest(
    name = name
)

fun ClassroomResponse.toDomain() = Classroom(
    name = name
)