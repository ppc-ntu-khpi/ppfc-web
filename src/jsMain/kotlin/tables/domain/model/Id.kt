/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

import kotlin.js.Date
import kotlin.random.Random

sealed class Id(val value: Long) {
    object Empty : Id(value = -1)
    class Value(value: Long) : Id(value = value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Id) return false

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {

        private val random = Random(Date().getMilliseconds())

        fun random() = Value(value = random.nextLong())
    }
}