/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes.model

import tables.domain.model.DayNumber
import tables.domain.model.WeekAlternation
import tables.presentation.common.mapper.toDayNumber
import kotlin.js.Date

data class ChangesCommonLessonState(
    val dayNumber: DayNumber = Date().toDayNumber(),
    val date: Date = Date(),
    val weekAlternation: WeekAlternation = WeekAlternation.NUMERATOR
) {
    companion object {
        val Empty = ChangesCommonLessonState()
    }
}
