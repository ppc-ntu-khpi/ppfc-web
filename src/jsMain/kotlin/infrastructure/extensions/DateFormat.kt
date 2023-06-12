/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package infrastructure.extensions

import kotlin.js.Date

/**
 * Pattern: yyyy-mm-dd.
 */
fun Date.toISO8601String() = this.toISOString().substringBefore('T')