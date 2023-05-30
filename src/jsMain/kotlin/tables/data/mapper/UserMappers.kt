/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.UserResponse
import tables.domain.model.Group
import tables.domain.model.Id
import tables.domain.model.Teacher
import tables.domain.model.User

fun UserResponse.toDomain(): User {
    return if (isGroup) {
        User.Group(
            id = Id.Value(value = id),
            group = group?.toDomain() ?: Group.Empty
        )
    } else {
        User.Teacher(
            id = Id.Value(value = id),
            teacher = teacher?.toDomain() ?: Teacher.Empty
        )
    }
}