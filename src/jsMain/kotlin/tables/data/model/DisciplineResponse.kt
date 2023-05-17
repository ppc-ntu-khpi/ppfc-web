package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DisciplineResponse(
    val id: Long,
    val name: String
)