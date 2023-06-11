/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.zip

fun <T, R> Flow<T>.onEachWithPrevious(
    action: suspend (old: T, new: T) -> R
): Flow<R> = this.zip(this.drop(1)) { old, new ->
    action(old, new)
}