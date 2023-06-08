/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.common.mapper

import coreui.theme.AppTheme
import tables.domain.model.DayNumber
import tables.presentation.common.model.DayNumberOption

fun DayNumberOption.toTextRepresentation() = when(this) {
    DayNumberOption.ALL -> AppTheme.stringResources.dayNumberAll
    DayNumberOption.N1 -> AppTheme.stringResources.monday
    DayNumberOption.N2 -> AppTheme.stringResources.tuesday
    DayNumberOption.N3 -> AppTheme.stringResources.wednesday
    DayNumberOption.N4 -> AppTheme.stringResources.thursday
    DayNumberOption.N5 -> AppTheme.stringResources.friday
    DayNumberOption.N6 -> AppTheme.stringResources.saturday
    DayNumberOption.N7 -> AppTheme.stringResources.sunday
}

fun DayNumber.toTextRepresentation() = when(this) {
    DayNumber.N1 -> AppTheme.stringResources.monday
    DayNumber.N2 -> AppTheme.stringResources.tuesday
    DayNumber.N3 -> AppTheme.stringResources.wednesday
    DayNumber.N4 -> AppTheme.stringResources.thursday
    DayNumber.N5 -> AppTheme.stringResources.friday
    DayNumber.N6 -> AppTheme.stringResources.saturday
    DayNumber.N7 -> AppTheme.stringResources.sunday
}

fun DayNumberOption.toDomain(): DayNumber? = when(this) {
    DayNumberOption.ALL -> null
    DayNumberOption.N1 -> DayNumber.N1
    DayNumberOption.N2 -> DayNumber.N2
    DayNumberOption.N3 -> DayNumber.N3
    DayNumberOption.N4 -> DayNumber.N4
    DayNumberOption.N5 -> DayNumber.N5
    DayNumberOption.N6 -> DayNumber.N6
    DayNumberOption.N7 -> DayNumber.N7
}