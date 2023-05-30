/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.domain.model.DayNumber

fun DayNumber.toNumber() = this.number

fun Long.toDayNumber() = DayNumber.values().find { it.number == this } ?: DayNumber.N1