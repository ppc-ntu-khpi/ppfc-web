package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubjectResponse(
    val id: Long,
    val name: String
)