/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.DisciplineRequest
import tables.data.model.DisciplineResponse
import tables.domain.model.Discipline
import tables.domain.model.Id

fun Discipline.toRequest() = DisciplineRequest(
    name = name
)

fun DisciplineResponse.toDomain() = Discipline(
    id = Id.Value(value = id),
    name = name
)