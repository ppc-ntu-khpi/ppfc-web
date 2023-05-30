/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.users

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import core.extensions.combine
import coreui.common.ApiCommonErrorMapper
import coreui.extensions.onSuccess
import coreui.extensions.withErrorMapper
import coreui.model.TextFieldState
import coreui.theme.AppTheme
import coreui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import tables.domain.interactor.DeleteUsers
import tables.domain.model.Id
import tables.domain.model.User
import tables.domain.observer.ObservePagedUsers

@OptIn(FlowPreview::class)
class UsersViewModel(
    private val observePagedUsers: ObservePagedUsers,
    private val deleteUsers: DeleteUsers,
    private val apiCommonErrorMapper: ApiCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<UsersViewEvent>()
    private val _dialog = MutableStateFlow<UsersDialog?>(null)
    private val _searchQuery = MutableStateFlow(TextFieldState.Empty)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())

    val pagedUsers: Flow<PagingData<User>> =
        observePagedUsers.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.cachedIn(coroutineScope)

    val state: StateFlow<UsersViewState> = combine(
        _searchQuery,
        _rowsSelection,
        loadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { searchQuery, rowsSelection, isLoading, isDeleting, dialog, event ->
        UsersViewState(
            searchQuery = searchQuery,
            rowsSelection = rowsSelection,
            isLoading = isLoading,
            isDeleting = isDeleting,
            dialog = dialog,
            event = event
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = UsersViewState.Empty,
    )

    init {
        _searchQuery.debounce(100).onEach { searchQuery ->
            observePagedUsers(searchQuery = searchQuery.text)
        }.launchIn(coroutineScope)
    }

    private fun observePagedUsers(
        searchQuery: String? = null
    ) {
        observePagedUsers(
            params = ObservePagedUsers.Params(
                searchQuery = searchQuery,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    fun setSearchQuery(searchQuery: String) {
        _searchQuery.update {
            it.copy(text = searchQuery)
        }
    }

    fun setRowSelection(id: Id, isSelected: Boolean) {
        _rowsSelection.update { rowsSelection ->
            rowsSelection.toMutableMap().apply {
                this[id] = isSelected
            }
        }
    }

    fun handlePagingError(cause: Throwable) {
        val message = apiCommonErrorMapper.map(cause = cause)
            ?: AppTheme.stringResources.unexpectedErrorException

        sendEvent(
            event = UsersViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun deleteUsers() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteUsers(
            params = DeleteUsers.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = UsersViewEvent.UserDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = UsersViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: UsersViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: UsersDialog?) {
        _dialog.value = dialog
    }

    fun clearEvent(id: Long) {
        uiEventManager.clearEvent(id = id)
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}