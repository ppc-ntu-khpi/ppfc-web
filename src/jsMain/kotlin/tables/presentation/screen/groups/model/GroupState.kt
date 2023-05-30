/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups.model

import coreui.model.NumberFieldState
import tables.domain.model.Course
import tables.domain.model.Id
import tables.presentation.compose.PagingDropDownMenuState

data class GroupState(
    val id: Id = Id.Empty,
    val number: NumberFieldState = NumberFieldState.Empty,
    val course: PagingDropDownMenuState<Course> = PagingDropDownMenuState.Empty()
) {
    companion object {
        val Empty = GroupState()
    }
}
