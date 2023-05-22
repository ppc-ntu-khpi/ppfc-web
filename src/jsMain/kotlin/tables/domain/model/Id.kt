/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

sealed class Id(val value: Long) {
    object Empty : Id(value = -1)
    class Value(value: Long) : Id(value = value)
}