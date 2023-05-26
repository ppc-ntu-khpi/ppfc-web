/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.extensions

import core.domain.InvokeStatus
import coreui.util.ErrorMapper
import coreui.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

fun <R> Flow<InvokeStatus<out R>>.withLoader(
    loadingCounter: ObservableLoadingCounter
) = onEach { status ->
    when (status) {
        InvokeStatus.InvokeStarted -> {
            loadingCounter.addLoader()
        }

        is InvokeStatus.InvokeSuccess -> {
            loadingCounter.removeLoader()
        }

        is InvokeStatus.InvokeError -> {
            loadingCounter.removeLoader()
        }
    }
}

fun <R> Flow<InvokeStatus<out R>>.onSuccess(
    onSuccess: (result: R) -> Unit
) = onEach { status ->
    if (status is InvokeStatus.InvokeSuccess) {
        onSuccess(status.result)
    }
}

fun <R> Flow<InvokeStatus<out R>>.onError(
    onError: (cause: Throwable) -> Unit
) = onEach { status ->
    if (status is InvokeStatus.InvokeError) {
        onError(status.cause)
    }
}

fun <R> Flow<InvokeStatus<out R>>.withErrorMapper(
    defaultMessage: String,
    errorMapper: ErrorMapper,
    exclude: Set<KClass<out Throwable>> = emptySet(),
    onErrorMessage: (message: String) -> Unit
) = onEach { status ->
    if (status is InvokeStatus.InvokeError) {
        if(exclude.any { it.isInstance(status.cause) }) return@onEach
        onErrorMessage(errorMapper.map(cause = status.cause) ?: defaultMessage)
    }
}