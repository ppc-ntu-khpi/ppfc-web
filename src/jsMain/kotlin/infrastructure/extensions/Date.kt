/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package infrastructure.extensions

import kotlin.js.Date
import kotlin.time.Duration.Companion.days

/**
 * @return yyyy-mm-dd formatted string
 */
fun Date.toISO8601DateString(): String = try {
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

/**
 * @return hh:mm formatted string
 */
fun Date.toISO8601TimeString(): String = try {
    this.toTimeString()
} catch (_: Throwable) {
    Date().toTimeString()
}.substringBeforeLast(':')


infix fun Date.plusDays(days: Long): Date {
    return Date(this.getTime() + days.days.inWholeMilliseconds)
}