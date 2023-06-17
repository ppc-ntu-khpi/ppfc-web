/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

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
import tables.domain.interactor.DeleteTeachers
import tables.domain.interactor.SaveTeacher
import tables.domain.model.Discipline
import tables.domain.model.Id
import tables.domain.model.Teacher
import tables.domain.observer.ObservePagedDisciplines
import tables.domain.observer.ObservePagedTeachers
import tables.extensions.onSearchQuery
import tables.presentation.compose.PagingDropDownMenuState

@OptIn(FlowPreview::class)
class TeachersViewModel(
    private val observePagedTeachers: ObservePagedTeachers,
    private val observePagedDisciplines: ObservePagedDisciplines,
    private val saveTeacher: SaveTeacher,
    private val deleteTeachers: DeleteTeachers,
    private val apiCommonErrorMapper: ApiCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val savingLoadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<TeachersViewEvent>()
    private val _dialog = MutableStateFlow<TeachersDialog?>(null)
    private val _searchQuery = MutableStateFlow(TextFieldState.Empty)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())
    private val _filterDiscipline = MutableStateFlow(PagingDropDownMenuState.Empty<Discipline>())

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.cachedIn(coroutineScope)

    val pagedDisciplines: Flow<PagingData<Discipline>> =
        observePagedDisciplines.flow.cachedIn(coroutineScope)

    val state: StateFlow<TeachersViewState> = combine(
        _searchQuery,
        _rowsSelection,
        _filterDiscipline,
        loadingState.observable,
        savingLoadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { searchQuery, rowsSelection, filterDiscipline, isLoading, isSaving, isDeleting, dialog, event ->
        TeachersViewState(
            searchQuery = searchQuery,
            rowsSelection = rowsSelection,
            filterDisciplinesMenu = filterDiscipline,
            isLoading = isLoading,
            isSaving = isSaving,
            isDeleting = isDeleting,
            dialog = dialog,
            event = event
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = TeachersViewState.Empty,
    )

    init {
        combine(
            _searchQuery.debounce(100),
            _filterDiscipline
        ) { searchQuery, filterDiscipline ->
            observePagedTeachers(
                searchQuery = searchQuery.text,
                discipline = filterDiscipline.selectedItem
            )
        }.launchIn(coroutineScope)

        _filterDiscipline.onSearchQuery { searchQuery ->
            observePagedDisciplines(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
    }

    private fun observePagedTeachers(
        searchQuery: String? = null,
        discipline: Discipline? = null
    ) {
        observePagedTeachers(
            params = ObservePagedTeachers.Params(
                searchQuery = searchQuery,
                discipline = discipline,
                pagingConfig = PAGING_CONFIG
            )
        )
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

    fun setFilterDiscipline(filterDisciplinesMenu: PagingDropDownMenuState<Discipline>) {
        _filterDiscipline.value = filterDisciplinesMenu
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
            event = TeachersViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun saveTeacher(teacher: Teacher) = launchWithLoader(savingLoadingState) {
        val teacherToSave = teacher.copy(
            firstName = teacher.firstName.trim(),
            lastName = teacher.lastName.trim(),
            middleName = teacher.middleName.trim()
        )

        saveTeacher(
            params = SaveTeacher.Params(
                teacher = teacherToSave
            )
        ).onSuccess {
            sendEvent(
                event = TeachersViewEvent.TeacherSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = TeachersViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun deleteTeachers() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteTeachers(
            params = DeleteTeachers.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = TeachersViewEvent.TeacherDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = TeachersViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: TeachersViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: TeachersDialog?) {
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