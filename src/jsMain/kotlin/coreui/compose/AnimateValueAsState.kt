/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.js.Date
import kotlin.time.Duration

@Composable
fun AnimateFloatAsState(
    key: Any? = null,
    duration: Duration,
    from: Float,
    to: Float
): State<Float> {
    val animatedValue = remember { mutableStateOf(from) }

    LaunchedEffect(key) {
        try {
            withTimeout(duration.inWholeMilliseconds) {
                val startTime = Date.now()
                val endTime = startTime + duration.inWholeMilliseconds

                while (Date.now() < endTime) {
                    val progress = (Date.now() - startTime).toFloat() / duration.inWholeMilliseconds
                    animatedValue.value = from + (to - from) * progress
                    delay(10)
                }
            }
        } finally {
            animatedValue.value = to
        }
    }

    return animatedValue
}