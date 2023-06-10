/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

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
import tables.domain.interactor.DeleteScheduleItems
import tables.domain.interactor.SaveScheduleItem
import tables.domain.interactor.SaveScheduleItems
import tables.domain.model.*
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedSchedule
import tables.domain.observer.ObservePagedTeachers
import tables.presentation.common.mapper.TablesCommonErrorMapper
import tables.presentation.common.mapper.toDomain
import tables.presentation.common.model.DayNumberOption
import tables.presentation.common.model.WeekAlternationOption
import tables.presentation.compose.PagingDropDownMenuState

class ScheduleViewModel(
    private val observePagedSchedule: ObservePagedSchedule,
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedTeachers: ObservePagedTeachers,
    private val saveScheduleItem: SaveScheduleItem,
    private val saveScheduleItems: SaveScheduleItems,
    private val deleteScheduleItems: DeleteScheduleItems,
    private val apiCommonErrorMapper: ApiCommonErrorMapper,
    private val tablesCommonErrorMapper: TablesCommonErrorMapper
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val loadingState = ObservableLoadingCounter()
    private val savingLoadingState = ObservableLoadingCounter()
    private val deletingLoadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<ScheduleViewEvent>()
    private val _dialog = MutableStateFlow<ScheduleDialog?>(null)
    private val _rowsSelection = MutableStateFlow(mapOf<Id, Boolean>())
    private val _filterGroup = MutableStateFlow(PagingDropDownMenuState.Empty<Group>())
    private val _filterTeacher = MutableStateFlow(PagingDropDownMenuState.Empty<Teacher>())
    private val _filterDayNumber = MutableStateFlow(DayNumberOption.ALL)
    private val _filterWeekAlternation = MutableStateFlow(WeekAlternationOption.ALL)

    val pagedSchedule: Flow<PagingData<ScheduleItem>> =
        observePagedSchedule.flow.onEach {
            _rowsSelection.value = emptyMap()
        }.cachedIn(coroutineScope)

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.cachedIn(coroutineScope)

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.cachedIn(coroutineScope)

    val state: StateFlow<ScheduleViewState> = combine(
        _rowsSelection,
        _filterGroup,
        _filterTeacher,
        _filterDayNumber,
        _filterWeekAlternation,
        loadingState.observable,
        savingLoadingState.observable,
        deletingLoadingState.observable,
        _dialog,
        uiEventManager.event
    ) { rowsSelection, filterGroup, filterTeacher, filterDayNumber, filterWeekAlternation, isLoading, isSaving, isDeleting, dialog, event ->
        ScheduleViewState(
            rowsSelection = rowsSelection,
            filterGroup = filterGroup,
            filterTeacher = filterTeacher,
            filterDayNumber = filterDayNumber,
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
        initialValue = ScheduleViewState.Empty,
    )

    init {
        combine(
            _filterGroup,
            _filterTeacher,
            _filterDayNumber,
            _filterWeekAlternation
        ) { filterGroup, filterTeacher, filterDayNumber, filterWeekAlternation ->
            observePagedSchedule(
                dayNumber = filterDayNumber.toDomain(),
                weekAlternation = filterWeekAlternation.toDomain(),
                group = filterGroup.selectedItem,
                teacher = filterTeacher.selectedItem
            )
        }.launchIn(coroutineScope)

        _filterGroup.map { it.searchQuery }
            .distinctUntilChanged()
            .onEach { searchQuery ->
                observePagedGroups(
                    searchQuery = searchQuery
                )
            }.launchIn(coroutineScope)

        _filterTeacher.map { it.searchQuery }
            .distinctUntilChanged()
            .onEach { searchQuery ->
                observePagedTeachers(
                    searchQuery = searchQuery
                )
            }.launchIn(coroutineScope)
    }

    private fun observePagedSchedule(
        dayNumber: DayNumber? = null,
        weekAlternation: WeekAlternation? = null,
        group: Group? = null,
        teacher: Teacher? = null
    ) {
        observePagedSchedule(
            params = ObservePagedSchedule.Params(
                dayNumber = dayNumber,
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
        _filterGroup.value = filterGroup
    }

    fun setFilterTeacher(filterTeacher: PagingDropDownMenuState<Teacher>) {
        _filterTeacher.value = filterTeacher
    }

    fun setFilterDayNumber(filterDayNumber: DayNumberOption) {
        _filterDayNumber.value = filterDayNumber
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
            event = ScheduleViewEvent.Message(
                message = UiMessage(message = message)
            )
        )
    }

    fun saveScheduleItem(scheduleItem: ScheduleItem) = launchWithLoader(savingLoadingState) {
        saveScheduleItem(
            params = SaveScheduleItem.Params(
                scheduleItem = scheduleItem
            )
        ).onSuccess {
            sendEvent(
                event = ScheduleViewEvent.ScheduleItemSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper + tablesCommonErrorMapper
        ) { message ->
            sendEvent(
                event = ScheduleViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun saveScheduleItems(scheduleItems: List<ScheduleItem>) = launchWithLoader(savingLoadingState) {
        saveScheduleItems(
            params = SaveScheduleItems.Params(
                scheduleItems = scheduleItems
            )
        ).onSuccess {
            sendEvent(
                event = ScheduleViewEvent.ScheduleItemSaved
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper + tablesCommonErrorMapper
        ) { message ->
            sendEvent(
                event = ScheduleViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    fun deleteScheduleItems() = launchWithLoader(deletingLoadingState) {
        val idsToDelete = _rowsSelection.value.filter { it.value }.map { it.key }.toSet()

        deleteScheduleItems(
            params = DeleteScheduleItems.Params(ids = idsToDelete)
        ).onSuccess {
            sendEvent(
                event = ScheduleViewEvent.ScheduleItemDeleted
            )
        }.withErrorMapper(
            defaultMessage = AppTheme.stringResources.unexpectedErrorException,
            errorMapper = apiCommonErrorMapper
        ) { message ->
            sendEvent(
                event = ScheduleViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: ScheduleViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun dialog(dialog: ScheduleDialog?) {
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