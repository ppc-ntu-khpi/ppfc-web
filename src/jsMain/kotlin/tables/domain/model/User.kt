/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

sealed class User(val id: Id) {
    class Group(id: Id, val group: tables.domain.model.Group) : User(id = id)
    class Teacher(id: Id, val teacher: tables.domain.model.Teacher) : User(id = id)
}