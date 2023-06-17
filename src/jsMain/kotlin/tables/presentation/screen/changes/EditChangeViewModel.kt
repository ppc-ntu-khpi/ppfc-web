/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import coreui.model.TextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.interactor.calculateWeekAlternation
import tables.domain.model.*
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedSubjects
import tables.domain.observer.ObservePagedTeachers
import tables.extensions.onSearchQuery
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.changes.model.ChangeLessonNumberOption
import tables.presentation.screen.changes.model.ChangeState
import kotlin.js.Date

class EditChangeViewModel(
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedClassrooms: ObservePagedClassrooms,
    private val observePagedTeachers: ObservePagedTeachers,
    private val observePagedSubjects: ObservePagedSubjects
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _changeState = MutableStateFlow(ChangeState.Empty)
    private val isFormBlank = _changeState.map { changeState ->
        changeState.selectedGroups.isEmpty()
                || (changeState.subjectsMenu.selectedItem == null
                && changeState.eventName == TextFieldState.Empty)
    }
    private val canAddGroups = _changeState.map { change ->
        change.selectedGroups.size < 20
    }

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.cachedIn(coroutineScope)

    val pagedClassrooms: Flow<PagingData<Classroom>> =
        observePagedClassrooms.flow.cachedIn(coroutineScope)

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.cachedIn(coroutineScope)

    val pagedSubjects: Flow<PagingData<Subject>> =
        observePagedSubjects.flow.cachedIn(coroutineScope)

    val state: StateFlow<EditChangeViewState> = combine(
        _changeState,
        isFormBlank,
        canAddGroups
    ) { changeState, isFormBlank, canAddGroups ->
        EditChangeViewState(
            changeState = changeState,
            isFormBlank = isFormBlank,
            canAddGroups = canAddGroups
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = EditChangeViewState.Empty,
    )

    init {
        observePagedGroups()
        observePagedClassrooms()
        observePagedTeachers()
        observePagedSubjects()

        _changeState.map { it.groupsMenu }.onSearchQuery { searchQuery ->
            observePagedGroups(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _changeState.map { it.classroomsMenu }.onSearchQuery { searchQuery ->
            observePagedClassrooms(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _changeState.map { it.teachersMenu }.onSearchQuery { searchQuery ->
            observePagedTeachers(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _changeState.map { it.subjectsMenu }.onSearchQuery { searchQuery ->
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

    fun loadChangeState(changeState: ChangeState) {
        _changeState.value = changeState
    }

    fun addGroup(group: Group) {
        _changeState.update { item ->
            if (item.selectedGroups.size >= 20) return@update item
            item.copy(selectedGroups =  item.selectedGroups + group)
        }
    }

    fun removeGroup(group: Group) {
        _changeState.update { item ->
            item.copy(selectedGroups = item.selectedGroups - group)
        }
    }

    fun setGroup(groupsMenu: PagingDropDownMenuState<Group>) {
        _changeState.update {
            it.copy(groupsMenu = groupsMenu.copy(selectedItem = null))
        }
    }

    fun setClassroom(classroomsMenu: PagingDropDownMenuState<Classroom>) {
        _changeState.update {
            it.copy(classroomsMenu = classroomsMenu)
        }
    }

    fun setTeacher(teachersMenu: PagingDropDownMenuState<Teacher>) {
        _changeState.update {
            it.copy(teachersMenu = teachersMenu)
        }
    }

    fun setEventName(eventName: String) {
        _changeState.update {
            it.copy(
                eventName = it.eventName.copy(text = eventName),
                subjectsMenu = PagingDropDownMenuState.Empty()
            )
        }
    }

    fun setSubject(subjectsMenu: PagingDropDownMenuState<Subject>) {
        _changeState.update {
            it.copy(
                subjectsMenu = subjectsMenu,
                eventName = TextFieldState.Empty
            )
        }
    }

    fun setDate(date: Date) {
        _changeState.update {
            it.copy(
                date = date,
                weekAlternation = date.calculateWeekAlternation()
            )
        }
    }

    fun setDayNumber(dayNumber: DayNumber) {
        _changeState.update {
            it.copy(dayNumber = dayNumber)
        }
    }

    fun setLessonNumber(lessonNumber: ChangeLessonNumberOption) {
        _changeState.update {
            it.copy(lessonNumber = lessonNumber)
        }
    }

    fun setWeekAlternation(weekAlternation: WeekAlternation) {
        _changeState.update {
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