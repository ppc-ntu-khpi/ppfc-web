package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClassroomResponse(
    val id: Long,
    val name: String
)