/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

enum class WeekAlternation(val isNumerator: Boolean) {
    NUMERATOR(isNumerator = true),
    DENOMINATOR(isNumerator = false)
}