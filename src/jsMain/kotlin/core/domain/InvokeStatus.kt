/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package core.domain

sealed class InvokeStatus<R> {
    object InvokeStarted : InvokeStatus<Nothing>()
    class InvokeSuccess<R>(val result: R) : InvokeStatus<R>()
    class InvokeError(val cause: Throwable) : InvokeStatus<Nothing>()
}