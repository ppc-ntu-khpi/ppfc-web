/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects.mapper

import coreui.model.TextFieldState
import tables.domain.model.Subject
import tables.presentation.screen.subjects.model.SubjectState

fun SubjectState.toDomain() = Subject(
    id = id,
    name = name.text
)

fun Subject.toState() = SubjectState(
    id = id,
    name = TextFieldState.Empty.copy(text = name)
)