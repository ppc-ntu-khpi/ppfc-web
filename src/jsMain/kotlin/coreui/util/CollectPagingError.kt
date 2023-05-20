/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paging.CombinedLoadStates
import app.cash.paging.LoadStateError

@Composable
fun CollectPagingError(
    combinedLoadStates: CombinedLoadStates,
    onError: (cause: Throwable) -> Unit
) {
    LaunchedEffect(
        combinedLoadStates.refresh is LoadStateError ||
                combinedLoadStates.append is LoadStateError ||
                combinedLoadStates.prepend is LoadStateError
    ) {
        combinedLoadStates.source.forEach { _, loadState ->
            if (loadState is LoadStateError) {
                onError(loadState.error)
            }
        }
    }
}