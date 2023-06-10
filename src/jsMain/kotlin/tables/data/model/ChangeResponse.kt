/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangeResponse(
    val id: Long,
    val group: GroupResponse,
    val classroom: ClassroomResponse,
    val teacher: TeacherResponse?,
    val subject: SubjectResponse?,
    val eventName: String?,
    val isSubject: Boolean,
    val lessonNumber: Long,
    val date: String,
    val isNumerator: Boolean
)