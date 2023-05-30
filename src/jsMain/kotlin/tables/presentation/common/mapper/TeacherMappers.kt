/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.common.mapper

import tables.domain.model.Teacher

fun Teacher.toTextRepresentation() = with(this) {
    "$lastName $firstName $middleName"
}