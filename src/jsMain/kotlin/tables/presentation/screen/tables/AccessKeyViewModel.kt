/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.tables

import coreui.common.ApiCommonErrorMapper
import coreui.extensions.onSuccess
import coreui.extensions.withErrorMapper
import coreui.theme.AppTheme
import coreui.util.ObservableLoadingCounter
import coreui.util.launchWithLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.interactor.GenerateAccessKey
import tables.domain.model.AccessKey

class AccessKeyViewModel(
    private val generateAccessKey: GenerateAccessKey,
    private val apiCommonErrorMapper: ApiCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val _accessKey = MutableStateFlow(AccessKey.Empty)
    private val _errorLoading = MutableStateFlow(false)

    val state: StateFlow<AccessKeyViewState> = combine(
        loadingState.observable,
        _errorLoading,
        _accessKey
    ) { isLoading, errorLoading, accessKey ->
        AccessKeyViewState(
            isLoading = isLoading,
            errorLoading = errorLoading,
            accessKey = accessKey
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = AccessKeyViewState.Empty,
    )

    init {
        generateAccessKey()
    }

    fun generateAccessKey() = launchWithLoader(loadingState) {
        generateAccessKey(Unit).onSuccess { accessKey ->
            _accessKey.value = accessKey
            _errorLoading.value = false
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) {
            _errorLoading.value = true
        }.collect()
    }
}