/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun launchWithLoader(
    loadingCounter: ObservableLoadingCounter,
    block: suspend () -> Unit
) = CoroutineScope(Dispatchers.Default).launch {
        loadingCounter.addLoader()
        try {
            block()
        } finally {
            loadingCounter.removeLoader()
        }
    }