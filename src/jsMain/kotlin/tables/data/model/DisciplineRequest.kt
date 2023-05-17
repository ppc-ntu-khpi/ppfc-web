package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DisciplineRequest(
    val name: String
)