/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.model.Course
import tables.domain.observer.ObservePagedCourses
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.groups.model.GroupState

class ManageGroupViewModel(
    private val observePagedCourses: ObservePagedCourses
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _groupState = MutableStateFlow(GroupState.Empty)
    private val _isFormBlank = _groupState.map { groupState ->
        groupState.number.number == null
                || groupState.course.selectedItem == null
    }

    val pagedCourses: Flow<PagingData<Course>> =
        observePagedCourses.flow.cachedIn(coroutineScope)

    val state: StateFlow<ManageGroupViewState> = combine(
        _groupState,
        _isFormBlank
    ) { groupState, isFormBlank ->
        ManageGroupViewState(
            groupState = groupState,
            isFormBlank = isFormBlank
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ManageGroupViewState.Empty,
    )

    init {
        _groupState.onEach { groupState ->
            observePagedCourses(
                searchQuery = groupState.course.searchQuery
            )
        }.launchIn(coroutineScope)
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

    fun loadGroupState(groupState: GroupState) {
        _groupState.value = groupState
    }

    fun setNumber(number: Long?) {
        _groupState.update {
            it.copy(
                number = it.number.copy(
                    number = number,
                    error = null
                ),
            )
        }
    }

    fun setCourse(course: PagingDropDownMenuState<Course>) {
        _groupState.update {
            it.copy(course = course)
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}