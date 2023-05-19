package tables.presentation.screen.classrooms

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import core.domain.NetworkException
import core.domain.TimeoutException
import coreui.theme.AppTheme
import coreui.util.ObservableLoadingCounter
import coreui.util.UiEvent
import coreui.util.UiEventManager
import coreui.util.UiMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import onboarding.domain.interactor.AuthenticationException
import onboarding.domain.interactor.LogOut
import tables.domain.model.Classroom
import tables.domain.observer.ObservePagedClassrooms

class ClassroomsViewModel(
    observePagedClassrooms: ObservePagedClassrooms,
    private val logOut: LogOut
) {

    private val loadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<ClassroomsViewEvent>()

    val pagedClassrooms: Flow<PagingData<Classroom>> =
        observePagedClassrooms.flow.cachedIn(CoroutineScope(Dispatchers.Default))

    val state: StateFlow<ClassroomsViewState> = combine(
        loadingState.observable,
        uiEventManager.event
    ) { isLoading, event ->
        ClassroomsViewState(
            isLoading = isLoading,
            event = event
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ClassroomsViewState.Empty,
    )

    init {
        observePagedClassrooms(
            params = ObservePagedClassrooms.Params(
                searchQuery = "",
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    fun logOut() {
        logOut(Unit).launchIn(CoroutineScope(Dispatchers.Default))
    }

    fun handlePagingError(cause: Throwable) {
        val message = when (cause) {
            is NetworkException -> AppTheme.stringResources.networkException
            is TimeoutException -> AppTheme.stringResources.timeoutException
            is AuthenticationException -> AppTheme.stringResources.authenticationException
            else -> AppTheme.stringResources.unexpectedErrorException
        }

        sendEvent(
            event = ClassroomsViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    private fun sendEvent(event: ClassroomsViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun clearEvent(id: Long) {
        uiEventManager.clearEvent(id = id)
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10
        )
    }
}