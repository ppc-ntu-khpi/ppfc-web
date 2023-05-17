package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val id: Long,
    val groupId: Long?,
    val teacherId: Long?
)