/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.domain.model.WeekAlternation

fun Boolean.toWeekAlternation() = if(this) {
    WeekAlternation.NUMERATOR
} else {
    WeekAlternation.DENOMINATOR
}