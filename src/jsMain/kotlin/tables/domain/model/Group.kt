/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

data class Group(
    val id: Id = Id.Empty,
    val number: Long = 0L,
    val course: Course = Course.Empty
) {
    companion object {
        val Empty = Group()
    }
}