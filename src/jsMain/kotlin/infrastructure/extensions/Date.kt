/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package infrastructure.extensions

import kotlin.js.Date

/**
 * Pattern: yyyy-mm-dd.
 */
fun Date.toISO8601String(): String = try {
    this.toISOString()
} catch (_: Throwable) {
    Date().toISOString()
}.substringBefore('T')

fun Date.Companion.dateFromString(dateString: String): Date? = Date(dateString).let { date ->
    if (date.toString() == "Invalid Date") {
        null
    } else {
        date
    }
}