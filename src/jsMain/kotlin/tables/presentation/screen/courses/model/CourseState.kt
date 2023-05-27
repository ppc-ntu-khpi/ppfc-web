/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses.model

import coreui.model.NumberFieldState
import tables.domain.model.Id

data class CourseState(
    val id: Id = Id.Empty,
    val number: NumberFieldState = NumberFieldState.Empty
) {
    companion object {
        val Empty = CourseState()
    }
}
