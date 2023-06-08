/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.common.mapper

import coreui.theme.AppTheme
import tables.domain.model.WeekAlternation
import tables.presentation.common.model.WeekAlternationOption

fun WeekAlternationOption.toTextRepresentation() = when(this) {
    WeekAlternationOption.ALL -> AppTheme.stringResources.weekAlternationAll
    WeekAlternationOption.NUMERATOR -> AppTheme.stringResources.weekAlternationNumerator
    WeekAlternationOption.DENOMINATOR -> AppTheme.stringResources.weekAlternationDenominator
}

fun WeekAlternation.toTextRepresentation() = when(this) {
    WeekAlternation.NUMERATOR -> AppTheme.stringResources.weekAlternationNumerator
    WeekAlternation.DENOMINATOR -> AppTheme.stringResources.weekAlternationDenominator
}

fun WeekAlternationOption.toDomain(): WeekAlternation? = when(this) {
    WeekAlternationOption.ALL -> null
    WeekAlternationOption.NUMERATOR -> WeekAlternation.NUMERATOR
    WeekAlternationOption.DENOMINATOR -> WeekAlternation.DENOMINATOR
}