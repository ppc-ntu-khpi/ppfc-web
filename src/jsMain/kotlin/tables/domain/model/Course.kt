/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

data class Course(
    val id: Id = Id.Empty,
    val number: Long = 0L
) {
    companion object {
        val Empty = Course()
    }
}