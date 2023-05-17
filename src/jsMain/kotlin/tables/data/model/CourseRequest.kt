package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseRequest(
    val number: Long
)