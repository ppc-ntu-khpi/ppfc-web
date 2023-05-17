/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import kotlin.random.Random


data class UiMessage(
    val message: String,
    val actionTag: String? = null,
    val id: Long = Random.Default.nextLong()
)