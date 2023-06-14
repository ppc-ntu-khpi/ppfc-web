/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import tables.domain.model.Id
import tables.presentation.screen.changes.model.ChangeLessonState
import tables.presentation.screen.changes.model.ChangesCommonLessonState

data class CreateChangesViewState(
    val changesCommonLesson: ChangesCommonLessonState = ChangesCommonLessonState.Empty,
    val changesLessons: Map<Id.Value, ChangeLessonState> = mapOf(
        Id.random() to ChangeLessonState.Empty
    ),
    val isFormBlank: Boolean = true,
    val canAddLessons: Boolean = true,
    val canRemoveLessons: Boolean = false,
    val canAddGroups: Map<Id.Value, Boolean> = emptyMap()
) {
    companion object {
        val Empty = CreateChangesViewState()
    }
}