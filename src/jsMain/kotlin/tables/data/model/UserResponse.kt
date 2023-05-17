package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long,
    val group: GroupResponse?,
    val teacher: TeacherResponse?,
    val isGroup: Boolean
)