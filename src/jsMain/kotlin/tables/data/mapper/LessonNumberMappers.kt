/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.mapper

import tables.domain.model.LessonNumber

fun LessonNumber.toNumber() = this.number

fun Long.toLessonNumber() = LessonNumber.values().find { it.number == this } ?: LessonNumber.N1