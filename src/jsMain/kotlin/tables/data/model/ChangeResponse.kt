/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package org.ppfc.api.model.service.change

import kotlinx.serialization.Serializable
import tables.data.model.ClassroomResponse
import tables.data.model.GroupResponse
import tables.data.model.SubjectResponse
import tables.data.model.TeacherResponse

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