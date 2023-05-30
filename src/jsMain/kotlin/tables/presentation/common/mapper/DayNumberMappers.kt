/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.common.mapper

import coreui.theme.AppTheme
import tables.domain.model.DayNumber

fun DayNumber.toTextRepresentation() = when(this) {
    DayNumber.N1 -> AppTheme.stringResources.monday
    DayNumber.N2 -> AppTheme.stringResources.tuesday
    DayNumber.N3 -> AppTheme.stringResources.wednesday
    DayNumber.N4 -> AppTheme.stringResources.thursday
    DayNumber.N5 -> AppTheme.stringResources.friday
    DayNumber.N6 -> AppTheme.stringResources.saturday
    DayNumber.N7 -> AppTheme.stringResources.sunday
}