/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups.mapper

import coreui.model.NumberFieldState
import tables.domain.model.Course
import tables.domain.model.Group
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.groups.model.GroupState

fun GroupState.toDomain() = Group(
    id = id,
    number = number.number ?: 0L,
    course = course.selectedItem ?: Course.Empty
)

fun Group.toState() = GroupState(
    id = id,
    number = NumberFieldState.Empty.copy(number = number),
    course = PagingDropDownMenuState.Empty<Course>().copy(selectedItem = course)
)