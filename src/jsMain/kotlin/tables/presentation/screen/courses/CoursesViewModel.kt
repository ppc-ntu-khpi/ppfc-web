/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.map
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
import tables.domain.interactor.DeleteCourses
import tables.domain.interactor.SaveCourse
import tables.domain.model.Id
import tables.domain.observer.ObservePagedCourses
import tables.presentation.screen.courses.mapper.toDomain
import tables.presentation.screen.courses.mapper.toState
import tables.presentation.screen.courses.model.CourseState

@OptIn(FlowPreview::class)
class CoursesViewModel(
    private val observePagedCourses: ObservePagedCourses,
    private val saveCourse: SaveCourse,
    private val deleteCourses: DeleteCourses,
    private val apiCommonErrorMapper: ApiCommonErrorMapper
) {

    private val loadingState = ObservableLoadingCounter()
    private val savingLoadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<CoursesViewEvent>()
    private val _dialog = MutableStateFlow<CoursesDialog?>(null)
    private val _searchQuery = MutableStateFlow(TextFieldState.Empty)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())

    val pagedCourses: Flow<PagingData<CourseState>> =
        observePagedCourses.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.map { pagingData ->
            pagingData.map {
                it.toState()
            }
        }.cachedIn(CoroutineScope(Dispatchers.Default))

    val state: StateFlow<CoursesViewState> = combine(
        _searchQuery,
        _rowsSelection,
        loadingState.observable,
        savingLoadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { searchQuery, rowsSelection, isLoading, isSaving, isDeleting, dialog, event ->
        CoursesViewState(
            searchQuery = searchQuery,
            rowsSelection = rowsSelection,
            isLoading = isLoading,
            isSaving = isSaving,
            isDeleting = isDeleting,
            dialog = dialog,
            event = event
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = CoursesViewState.Empty,
    )

    init {
        _searchQuery.debounce(100).onEach { searchQuery ->
            observePagedCourses(searchQuery = searchQuery.text)
        }.launchIn(CoroutineScope(Dispatchers.Default))
    }

    private fun observePagedCourses(
        searchQuery: String = ""
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
            event = CoursesViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun saveCourse(courseState: CourseState) = launchWithLoader(savingLoadingState) {
        val course = courseState.toDomain().let {
            it.copy(
                number = it.number
            )
        }

        saveCourse(
            params = SaveCourse.Params(
                course = course
            )
        ).onSuccess {
            sendEvent(
                event = CoursesViewEvent.CourseSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = CoursesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun deleteCourses() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteCourses(
            params = DeleteCourses.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = CoursesViewEvent.CourseDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = CoursesViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: CoursesViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: CoursesDialog?) {
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