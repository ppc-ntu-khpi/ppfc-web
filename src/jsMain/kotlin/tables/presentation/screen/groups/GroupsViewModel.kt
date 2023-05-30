/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

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
import tables.domain.interactor.DeleteGroups
import tables.domain.interactor.SaveGroup
import tables.domain.model.Course
import tables.domain.model.Group
import tables.domain.model.Id
import tables.domain.observer.ObservePagedCourses
import tables.domain.observer.ObservePagedGroups
import tables.presentation.compose.PagingDropDownMenuState

@OptIn(FlowPreview::class)
class GroupsViewModel(
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedCourses: ObservePagedCourses,
    private val saveGroup: SaveGroup,
    private val deleteGroups: DeleteGroups,
    private val apiCommonErrorMapper: ApiCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val savingLoadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<GroupsViewEvent>()
    private val _dialog = MutableStateFlow<GroupsDialog?>(null)
    private val _searchQuery = MutableStateFlow(TextFieldState.Empty)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())
    private val _filterCourse = MutableStateFlow(PagingDropDownMenuState.Empty<Course>())

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.cachedIn(coroutineScope)

    val pagedCourses: Flow<PagingData<Course>> =
        observePagedCourses.flow.cachedIn(coroutineScope)

    val state: StateFlow<GroupsViewState> = combine(
        _searchQuery,
        _rowsSelection,
        _filterCourse,
        loadingState.observable,
        savingLoadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { searchQuery, rowsSelection, filterCourse, isLoading, isSaving, isDeleting, dialog, event ->
        GroupsViewState(
            searchQuery = searchQuery,
            rowsSelection = rowsSelection,
            filterCourse = filterCourse,
            isLoading = isLoading,
            isSaving = isSaving,
            isDeleting = isDeleting,
            dialog = dialog,
            event = event
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = GroupsViewState.Empty,
    )

    init {
        combine(
            _searchQuery.debounce(100),
            _filterCourse
        ) { searchQuery, filterCourse ->
            observePagedGroups(
                searchQuery = searchQuery.text,
                course = filterCourse.selectedItem
            )
        }.launchIn(coroutineScope)

        _filterCourse.onEach { filterCourse ->
            observePagedCourses(
                searchQuery = filterCourse.searchQuery
            )
        }.launchIn(coroutineScope)
    }

    private fun observePagedGroups(
        searchQuery: String? = null,
        course: Course? = null
    ) {
        observePagedGroups(
            params = ObservePagedGroups.Params(
                searchQuery = searchQuery,
                course = course,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    private fun observePagedCourses(
        searchQuery: String? = null
    ) {
        observePagedCourses(
            params = ObservePagedCourses.Params(
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

    fun setFilterCourse(filterCourse: PagingDropDownMenuState<Course>) {
        _filterCourse.value = filterCourse
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
            event = GroupsViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun saveGroup(group: Group) = launchWithLoader(savingLoadingState) {
        saveGroup(
            params = SaveGroup.Params(
                group = group
            )
        ).onSuccess {
            sendEvent(
                event = GroupsViewEvent.GroupSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = GroupsViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun deleteGroups() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteGroups(
            params = DeleteGroups.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = GroupsViewEvent.GroupDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = GroupsViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: GroupsViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: GroupsDialog?) {
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