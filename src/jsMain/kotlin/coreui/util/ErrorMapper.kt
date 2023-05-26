/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

fun interface ErrorMapper {
    fun map(cause: Throwable): String?
}