/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import core.extensions.combine
import coreui.common.ApiCommonErrorMapper
import coreui.extensions.onSuccess
import coreui.extensions.withErrorMapper
import coreui.theme.AppTheme
import coreui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.interactor.DeleteChanges
import tables.domain.interactor.SaveChange
import tables.domain.interactor.SaveChanges
import tables.domain.model.*
import tables.domain.observer.ObservePagedChanges
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedTeachers
import tables.extensions.onSearchQuery
import tables.presentation.common.mapper.TablesCommonErrorMapper
import tables.presentation.common.mapper.toDomain
import tables.presentation.common.model.WeekAlternationOption
import tables.presentation.compose.PagingDropDownMenuState

class ChangesViewModel(
    private val observePagedChanges: ObservePagedChanges,
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedTeachers: ObservePagedTeachers,
    private val saveChange: SaveChange,
    private val saveChanges: SaveChanges,
    private val deleteChanges: DeleteChanges,
    private val apiCommonErrorMapper: ApiCommonErrorMapper,
    private val tablesCommonErrorMapper: TablesCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val savingLoadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<ChangesViewEvent>()
    private val _dialog = MutableStateFlow<ChangesDialog?>(null)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())
    private val _filterGroup = MutableStateFlow(PagingDropDownMenuState.Empty<Group>())
    private val _filterTeacher = MutableStateFlow(PagingDropDownMenuState.Empty<Teacher>())
    private val _filterDate = MutableStateFlow<String?>(null)
    private val _filterWeekAlternation = MutableStateFlow(WeekAlternationOption.ALL)

    val pagedChanges: Flow<PagingData<Change>> =
        observePagedChanges.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.cachedIn(coroutineScope)

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.cachedIn(coroutineScope)

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.cachedIn(coroutineScope)

    val state: StateFlow<ChangesViewState> = combine(
        _rowsSelection,
        _filterGroup,
        _filterTeacher,
        _filterDate,
        _filterWeekAlternation,
        loadingState.observable,
        savingLoadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { rowsSelection, filterGroup, filterTeacher, filterDate, filterWeekAlternation, isLoading, isSaving, isDeleting, dialog, event ->
        ChangesViewState(
            rowsSelection = rowsSelection,
            filterGroup = filterGroup,
            filterTeacher = filterTeacher,
            filterDate = filterDate,
            filterWeekAlternation = filterWeekAlternation,
            isLoading = isLoading,
            isSaving = isSaving,
            isDeleting = isDeleting,
            dialog = dialog,
            event = event
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ChangesViewState.Empty,
    )

    init {
        combine(
            _filterGroup,
            _filterTeacher,
            _filterDate,
            _filterWeekAlternation
        ) { filterGroup, filterTeacher, filterDate, filterWeekAlternation ->
            observePagedChanges(
                date = filterDate,
                weekAlternation = filterWeekAlternation.toDomain(),
                group = filterGroup.selectedItem,
                teacher = filterTeacher.selectedItem
            )
        }.launchIn(coroutineScope)

        observePagedGroups()
        observePagedTeachers()

        _filterGroup.onSearchQuery { searchQuery ->
            observePagedGroups(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _filterTeacher.onSearchQuery { searchQuery ->
            observePagedTeachers(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
    }

    private fun observePagedChanges(
        date: String? = null,
        weekAlternation: WeekAlternation? = null,
        group: Group? = null,
        teacher: Teacher? = null
    ) {
        observePagedChanges(
            params = ObservePagedChanges.Params(
                date = date,
                weekAlternation = weekAlternation,
                group = group,
                teacher = teacher,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    private fun observePagedGroups(
        searchQuery: String? = null
    ) {
        observePagedGroups(
            params = ObservePagedGroups.Params(
                searchQuery = searchQuery,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    private fun observePagedTeachers(
        searchQuery: String? = null
    ) {
        observePagedTeachers(
            params = ObservePagedTeachers.Params(
                searchQuery = searchQuery,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    fun setFilterGroup(filterGroup: PagingDropDownMenuState<Group>) {
        _filterGroup.update {
            filterGroup
        }
    }

    fun setFilterTeacher(filterTeacher: PagingDropDownMenuState<Teacher>) {
        _filterTeacher.update {
            filterTeacher
        }
    }

    fun setFilterDate(filterDate: String?) {
        _filterDate.value = filterDate
    }

    fun setFilterWeekAlternation(filterWeekAlternation: WeekAlternationOption) {
        _filterWeekAlternation.value = filterWeekAlternation
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
            event = ChangesViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun saveChange(change: Change) = launchWithLoader(savingLoadingState) {
        saveChange(
            params = SaveChange.Params(
                change = change
            )
        ).onSuccess {
            sendEvent(
                event = ChangesViewEvent.ChangeSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper + tablesCommonErrorMapper
        ) { message ->
            sendEvent(
                event = ChangesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun saveChanges(changes: List<Change>) = launchWithLoader(savingLoadingState) {
        saveChanges(
            params = SaveChanges.Params(
                changes = changes
            )
        ).onSuccess {
            sendEvent(
                event = ChangesViewEvent.ChangeSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper + tablesCommonErrorMapper
        ) { message ->
            sendEvent(
                event = ChangesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun deleteChanges() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteChanges(
            params = DeleteChanges.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = ChangesViewEvent.ChangeDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = ChangesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: ChangesViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: ChangesDialog?) {
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