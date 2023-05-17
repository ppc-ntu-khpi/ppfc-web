package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ClassroomRequest(
    val name: String
)