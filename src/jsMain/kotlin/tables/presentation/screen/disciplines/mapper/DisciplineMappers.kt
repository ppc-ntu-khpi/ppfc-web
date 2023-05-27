/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines.mapper

import coreui.model.TextFieldState
import tables.domain.model.Discipline
import tables.presentation.screen.disciplines.model.DisciplineState

fun DisciplineState.toDomain() = Discipline(
    id = id,
    name = name.text
)

fun Discipline.toState() = DisciplineState(
    id = id,
    name = TextFieldState.Empty.copy(text = name)
)