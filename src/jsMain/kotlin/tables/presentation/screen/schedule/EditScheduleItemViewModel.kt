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
import tables.domain.model.*
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedSubjects
import tables.domain.observer.ObservePagedTeachers
import tables.extensions.onSearchQuery
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
    private val isFormBlank = _scheduleItemState.map { scheduleItemState ->
        scheduleItemState.group.selectedItem == null
                || scheduleItemState.classroom.selectedItem == null
                || scheduleItemState.teacher.selectedItem == null
                || (scheduleItemState.subject.selectedItem == null
                && scheduleItemState.eventName == TextFieldState.Empty)
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
        isFormBlank
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
        observePagedGroups()
        observePagedClassrooms()
        observePagedTeachers()
        observePagedSubjects()

        _scheduleItemState.map { it.group }.onSearchQuery { searchQuery ->
            observePagedGroups(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _scheduleItemState.map { it.classroom }.onSearchQuery { searchQuery ->
            observePagedClassrooms(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _scheduleItemState.map { it.teacher }.onSearchQuery { searchQuery ->
            observePagedTeachers(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _scheduleItemState.map { it.subject }.onSearchQuery { searchQuery ->
            observePagedSubjects(searchQuery = searchQuery)
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

    fun setEventName(eventName: String) {
        _scheduleItemState.update {
            it.copy(
                eventName = it.eventName.copy(text = eventName),
                subject = PagingDropDownMenuState.Empty()
            )
        }
    }

    fun setSubject(subject: PagingDropDownMenuState<Subject>) {
        _scheduleItemState.update {
            it.copy(
                subject = subject,
                eventName = TextFieldState.Empty
            )
        }
    }

    fun setDayNumber(dayNumber: DayNumber) {
        _scheduleItemState.update {
            it.copy(dayNumber = dayNumber)
        }
    }

    fun setLessonNumber(lessonNumber: LessonNumber) {
        _scheduleItemState.update {
            it.copy(lessonNumber = lessonNumber)
        }
    }

    fun setWeekAlternation(weekAlternation: WeekAlternation) {
        _scheduleItemState.update {
            it.copy(weekAlternation = weekAlternation)
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}