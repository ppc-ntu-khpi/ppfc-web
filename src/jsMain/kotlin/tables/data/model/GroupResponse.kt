package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupResponse(
    val id: Long,
    val number: Long,
    val course: CourseResponse
)