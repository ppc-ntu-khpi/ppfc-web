/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects

import tables.presentation.screen.subjects.model.SubjectState

data class ManageSubjectViewState(
    val subjectState: SubjectState = SubjectState.Empty,
    val isFormBlank: Boolean = true
) {
    companion object {
        val Empty = ManageSubjectViewState()
    }
}