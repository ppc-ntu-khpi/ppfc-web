/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

fun interface ErrorMapper {

    fun map(cause: Throwable): String?

    operator fun plus(other: ErrorMapper): ErrorMapper {
        val leftOperand = this
        return ErrorMapper { cause ->
            leftOperand.map(cause) ?: other.map(cause)
        }
    }
}