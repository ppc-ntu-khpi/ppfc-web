/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import tables.domain.model.WeekAlternation
import kotlin.js.Date
import kotlin.math.floor

fun Date.calculateWeekAlternation(): WeekAlternation {
    val lastSeptember1st = Date(year = this.getFullYear(), month = 8, day = 1)
    val timeDifference = this.getTime() - lastSeptember1st.getTime()
    val millisecondsInWeek = 604_800_000
    val weeksDifference = floor(timeDifference / millisecondsInWeek).toLong()

    return if(weeksDifference and 1 == 0L) {
        WeekAlternation.NUMERATOR
    } else {
        WeekAlternation.DENOMINATOR
    }
}