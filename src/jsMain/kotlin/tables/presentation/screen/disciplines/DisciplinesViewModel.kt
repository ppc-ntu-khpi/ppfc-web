/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

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
import tables.domain.interactor.DeleteDisciplines
import tables.domain.interactor.SaveDiscipline
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.domain.observer.ObservePagedDisciplines

@OptIn(FlowPreview::class)
class DisciplinesViewModel(
    private val observePagedDisciplines: ObservePagedDisciplines,
    private val saveDiscipline: SaveDiscipline,
    private val deleteDisciplines: DeleteDisciplines,
    private val apiCommonErrorMapper: ApiCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val savingLoadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<DisciplinesViewEvent>()
    private val _dialog = MutableStateFlow<DisciplinesDialog?>(null)
    private val _searchQuery = MutableStateFlow(TextFieldState.Empty)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())

    val pagedDisciplines: Flow<PagingData<Discipline>> =
        observePagedDisciplines.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.cachedIn(coroutineScope)

    val state: StateFlow<DisciplinesViewState> = combine(
        _searchQuery,
        _rowsSelection,
        loadingState.observable,
        savingLoadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { searchQuery, rowsSelection, isLoading, isSaving, isDeleting, dialog, event ->
        DisciplinesViewState(
            searchQuery = searchQuery,
            rowsSelection = rowsSelection,
            isLoading = isLoading,
            isSaving = isSaving,
            isDeleting = isDeleting,
            dialog = dialog,
            event = event
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DisciplinesViewState.Empty,
    )

    init {
        _searchQuery.debounce(100).onEach { searchQuery ->
            observePagedDisciplines(searchQuery = searchQuery.text)
        }.launchIn(coroutineScope)
    }

    private fun observePagedDisciplines(
        searchQuery: String? = null
    ) {
        observePagedDisciplines(
            params = ObservePagedDisciplines.Params(
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
            event = DisciplinesViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun saveDiscipline(discipline: Discipline) = launchWithLoader(savingLoadingState) {
        val disciplineToSave = discipline.copy(
            name = discipline.name.trim()
        )

        saveDiscipline(
            params = SaveDiscipline.Params(
                discipline = disciplineToSave
            )
        ).onSuccess {
            sendEvent(
                event = DisciplinesViewEvent.DisciplineSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = DisciplinesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun deleteDisciplines() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteDisciplines(
            params = DeleteDisciplines.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = DisciplinesViewEvent.DisciplineDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = DisciplinesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: DisciplinesViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: DisciplinesDialog?) {
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