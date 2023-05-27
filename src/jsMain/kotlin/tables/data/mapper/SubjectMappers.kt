/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.data.model.SubjectRequest
import tables.data.model.SubjectResponse
import tables.domain.model.Id
import tables.domain.model.Subject

fun Subject.toRequest() = SubjectRequest(
    name = name
)

fun SubjectResponse.toDomain() = Subject(
    id = Id.Value(value = id),
    name = name
)