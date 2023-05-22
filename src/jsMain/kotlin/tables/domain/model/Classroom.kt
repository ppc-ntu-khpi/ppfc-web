/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

data class Classroom(
    val id: Id = Id.Empty,
    val name: String = ""
) {
    companion object {
        val Empty = Classroom()
    }
}