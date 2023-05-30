/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

data class Teacher(
    val id: Id = Id.Empty,
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val discipline: Discipline = Discipline.Empty,
    val isHeadTeacher: Boolean = false
) {
    companion object {
        val Empty = Teacher()
    }
}