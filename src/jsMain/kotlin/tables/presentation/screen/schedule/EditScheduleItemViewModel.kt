/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import coreui.model.TextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.model.Classroom
import tables.domain.model.Group
import tables.domain.model.Subject
import tables.domain.model.Teacher
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedSubjects
import tables.domain.observer.ObservePagedTeachers
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.schedule.model.ScheduleItemState

class EditScheduleItemViewModel(
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedClassrooms: ObservePagedClassrooms,
    private val observePagedTeachers: ObservePagedTeachers,
    private val observePagedSubjects: ObservePagedSubjects
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _scheduleItemState = MutableStateFlow(ScheduleItemState.Empty)
    private val _isFormBlank = _scheduleItemState.map { scheduleItemState ->
        scheduleItemState.group.selectedItem == null
                || scheduleItemState.classroom.selectedItem == null
                || scheduleItemState.teacher.selectedItem == null
                || scheduleItemState.subject.selectedItem == null
                || scheduleItemState.eventName == TextFieldState.Empty
    }

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.cachedIn(coroutineScope)

    val pagedClassrooms: Flow<PagingData<Classroom>> =
        observePagedClassrooms.flow.cachedIn(coroutineScope)

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.cachedIn(coroutineScope)

    val pagedSubjects: Flow<PagingData<Subject>> =
        observePagedSubjects.flow.cachedIn(coroutineScope)

    val state: StateFlow<EditScheduleItemViewState> = combine(
        _scheduleItemState,
        _isFormBlank
    ) { scheduleItemState, isFormBlank ->
        EditScheduleItemViewState(
            scheduleItemState = scheduleItemState,
            isFormBlank = isFormBlank
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = EditScheduleItemViewState.Empty,
    )

    init {
        _scheduleItemState.onEach { scheduleItemState ->
            observePagedGroups(
                searchQuery = scheduleItemState.group.searchQuery
            )

            observePagedClassrooms(
                searchQuery = scheduleItemState.classroom.searchQuery
            )

            observePagedTeachers(
                searchQuery = scheduleItemState.teacher.searchQuery
            )

            observePagedSubjects(
                searchQuery = scheduleItemState.subject.searchQuery
            )
        }.launchIn(coroutineScope)
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

    private fun observePagedClassrooms(
        searchQuery: String? = null
    ) {
        observePagedClassrooms(
            params = ObservePagedClassrooms.Params(
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

    private fun observePagedSubjects(
        searchQuery: String? = null
    ) {
        observePagedSubjects(
            params = ObservePagedSubjects.Params(
                searchQuery = searchQuery,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    fun loadScheduleItemState(scheduleItemState: ScheduleItemState) {
        _scheduleItemState.value = scheduleItemState
    }

    fun setGroup(group: PagingDropDownMenuState<Group>) {
        _scheduleItemState.update {
            it.copy(group = group)
        }
    }

    fun setClassroom(classroom: PagingDropDownMenuState<Classroom>) {
        _scheduleItemState.update {
            it.copy(classroom = classroom)
        }
    }

    fun setTeacher(teacher: PagingDropDownMenuState<Teacher>) {
        _scheduleItemState.update {
            it.copy(teacher = teacher)
        }
    }

    fun setSubject(subject: PagingDropDownMenuState<Subject>) {
        _scheduleItemState.update {
            it.copy(subject = subject)
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}