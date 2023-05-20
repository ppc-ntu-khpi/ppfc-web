/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import coreui.util.UiEvent

data class ClassroomsViewState(
    val isLoading: Boolean = false,
    val event: UiEvent<ClassroomsViewEvent>? = null
) {
    companion object {
        val Empty = ClassroomsViewState()
    }
}