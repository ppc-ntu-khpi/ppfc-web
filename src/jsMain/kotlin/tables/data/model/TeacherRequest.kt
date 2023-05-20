/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherRequest(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val disciplineId: Long,
    val isHeadTeacher: Boolean
)