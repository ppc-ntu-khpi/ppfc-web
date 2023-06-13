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
import tables.domain.model.*
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedSubjects
import tables.domain.observer.ObservePagedTeachers
import tables.extensions.onItem
import tables.extensions.onSearchQuery
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.changes.model.ChangeState
import kotlin.js.Date

class CreateChangesViewModel(
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedClassrooms: ObservePagedClassrooms,
    private val observePagedTeachers: ObservePagedTeachers,
    private val observePagedSubjects: ObservePagedSubjects
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _changesStates = MutableStateFlow(listOf(ChangeState.Empty))
    private val isFormBlank = _changesStates.map { changesStates ->
        changesStates.any { changeState ->
            changeState.group.selectedItem == null
                    || changeState.classroom.selectedItem == null
                    || (changeState.subject.selectedItem == null
                    && changeState.eventName == TextFieldState.Empty)
        }
    }
    private val canAddItems = _changesStates.map { changesStates ->
        changesStates.size < LessonNumber.values().size * 2
    }
    private val canRemoveItems = _changesStates.map { changesStates ->
        changesStates.size > 1
    }

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.cachedIn(coroutineScope)

    val pagedClassrooms: Flow<PagingData<Classroom>> =
        observePagedClassrooms.flow.cachedIn(coroutineScope)

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.cachedIn(coroutineScope)

    val pagedSubjects: Flow<PagingData<Subject>> =
        observePagedSubjects.flow.cachedIn(coroutineScope)

    val state: StateFlow<CreateChangesViewState> = combine(
        _changesStates,
        isFormBlank,
        canAddItems,
        canRemoveItems
    ) { changesStates, isFormBlank, canAddItems, canRemoveItems ->
        CreateChangesViewState(
            changesStates = changesStates,
            isFormBlank = isFormBlank,
            canAddItems = canAddItems,
            canRemoveItems = canRemoveItems
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = CreateChangesViewState.Empty,
    )

    init {
        observePagedGroups()
        observePagedClassrooms()
        observePagedTeachers()
        observePagedSubjects()

        val pagingGroups = _changesStates.map { items -> items.map { it.group } }
        pagingGroups.onSearchQuery { searchQuery ->
            observePagedGroups(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
        pagingGroups.onItem { observePagedGroups() }.launchIn(coroutineScope)

        val pagingClassrooms = _changesStates.map { items -> items.map { it.classroom } }
        pagingClassrooms.onSearchQuery { searchQuery ->
            observePagedClassrooms(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
        pagingClassrooms.onItem { observePagedClassrooms() }.launchIn(coroutineScope)

        val pagingTeachers = _changesStates.map { items -> items.map { it.teacher } }
        pagingTeachers.onSearchQuery { searchQuery ->
            observePagedTeachers(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
        pagingTeachers.onItem { observePagedTeachers() }.launchIn(coroutineScope)

        val pagingSubjects = _changesStates.map { items -> items.map { it.subject } }
        pagingSubjects.onSearchQuery { searchQuery ->
            observePagedSubjects(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
        pagingSubjects.onItem { observePagedSubjects() }.launchIn(coroutineScope)
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

    fun addChange() {
        _changesStates.update { items ->
            if (items.size >= LessonNumber.values().size * 2) return@update items
            val lastLessonNumberOrdinal = items.lastOrNull()?.lessonNumber?.ordinal ?: 0
            val nextLessonNumber = LessonNumber.values().getOrElse(lastLessonNumberOrdinal + 1) {
                LessonNumber.N5
            }
            items + ChangeState.Empty.copy(
                group = items.first().group,
                date = items.first().date,
                lessonNumber = nextLessonNumber
            )
        }
    }

    fun removeChange(index: Long) {
        _changesStates.update { items ->
            if (items.size <= 1) return@update items
            items.filterIndexed { itemIndex, _ -> itemIndex.toLong() != index }
        }
    }

    fun setGroup(group: PagingDropDownMenuState<Group>) {
        _changesStates.update { items ->
            items.map { item ->
                item.copy(group = group)
            }
        }
    }

    fun setDate(date: Date) {
        _changesStates.update { items ->
            items.map { item ->
                item.copy(date = date)
            }
        }
    }

    fun setClassroom(index: Long, classroom: PagingDropDownMenuState<Classroom>) {
        _changesStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(classroom = classroom)
            }
        }
    }

    fun setTeacher(index: Long, teacher: PagingDropDownMenuState<Teacher>) {
        _changesStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(teacher = teacher)
            }
        }
    }

    fun setEventName(index: Long, eventName: String) {
        _changesStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(
                    eventName = item.eventName.copy(text = eventName),
                    subject = PagingDropDownMenuState.Empty()
                )
            }
        }
    }

    fun setSubject(index: Long, subject: PagingDropDownMenuState<Subject>) {
        _changesStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(
                    subject = subject,
                    eventName = TextFieldState.Empty
                )
            }
        }
    }

    fun setLessonNumber(index: Long, lessonNumber: LessonNumber) {
        _changesStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(lessonNumber = lessonNumber)
            }
        }
    }

    fun setWeekAlternation(index: Long, weekAlternation: WeekAlternation) {
        _changesStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(weekAlternation = weekAlternation)
            }
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}