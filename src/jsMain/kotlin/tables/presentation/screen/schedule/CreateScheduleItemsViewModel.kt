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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import tables.domain.model.*
import tables.domain.observer.ObservePagedClassrooms
import tables.domain.observer.ObservePagedGroups
import tables.domain.observer.ObservePagedSubjects
import tables.domain.observer.ObservePagedTeachers
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.schedule.model.ScheduleItemState

@OptIn(FlowPreview::class)
class CreateScheduleItemsViewModel(
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedClassrooms: ObservePagedClassrooms,
    private val observePagedTeachers: ObservePagedTeachers,
    private val observePagedSubjects: ObservePagedSubjects
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _scheduleItemsStates = MutableStateFlow(listOf(ScheduleItemState.Empty))
    private val isFormBlank = _scheduleItemsStates.map { scheduleItemsStates ->
        scheduleItemsStates.any { scheduleItemState ->
            scheduleItemState.group.selectedItem == null
                    || scheduleItemState.classroom.selectedItem == null
                    || scheduleItemState.teacher.selectedItem == null
                    || (scheduleItemState.subject.selectedItem == null
                    && scheduleItemState.eventName == TextFieldState.Empty)
        }
    }
    private val canAddItems = _scheduleItemsStates.map { scheduleItemState ->
        scheduleItemState.size < LessonNumber.values().size * 2
    }
    private val canRemoveItems = _scheduleItemsStates.map { scheduleItemState ->
        scheduleItemState.size > 1
    }

    val pagedGroups: Flow<PagingData<Group>> =
        observePagedGroups.flow.cachedIn(coroutineScope)

    val pagedClassrooms: Flow<PagingData<Classroom>> =
        observePagedClassrooms.flow.cachedIn(coroutineScope)

    val pagedTeachers: Flow<PagingData<Teacher>> =
        observePagedTeachers.flow.cachedIn(coroutineScope)

    val pagedSubjects: Flow<PagingData<Subject>> =
        observePagedSubjects.flow.cachedIn(coroutineScope)

    val state: StateFlow<CreateScheduleItemsViewState> = combine(
        _scheduleItemsStates,
        isFormBlank,
        canAddItems,
        canRemoveItems
    ) { scheduleItemsStates, isFormBlank, canAddItems, canRemoveItems ->
        CreateScheduleItemsViewState(
            scheduleItemsStates = scheduleItemsStates,
            isFormBlank = isFormBlank,
            canAddItems = canAddItems,
            canRemoveItems = canRemoveItems
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = CreateScheduleItemsViewState.Empty,
    )

    init {
        _scheduleItemsStates.flatMapConcat { items ->
            items.map {
                it.group.searchQuery
            }.asFlow()
        }.onEach { searchQuery ->
            observePagedGroups(
                searchQuery = searchQuery
            )
        }.launchIn(coroutineScope)

        _scheduleItemsStates.flatMapConcat { items ->
            items.map {
                it.classroom.searchQuery
            }.asFlow()
        }.onEach { searchQuery ->
            observePagedClassrooms(
                searchQuery = searchQuery
            )
        }.launchIn(coroutineScope)

        _scheduleItemsStates.flatMapConcat { items ->
            items.map {
                it.teacher.searchQuery
            }.asFlow()
        }.onEach { searchQuery ->
            observePagedTeachers(
                searchQuery = searchQuery
            )
        }.launchIn(coroutineScope)

        _scheduleItemsStates.flatMapConcat { items ->
            items.map {
                it.subject.searchQuery
            }.asFlow()
        }.onEach { searchQuery ->
            observePagedSubjects(
                searchQuery = searchQuery
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

    fun addScheduleItem() {
        _scheduleItemsStates.update { items ->
            if (items.size >= LessonNumber.values().size * 2) return@update items
            val lastLessonNumberOrdinal = items.lastOrNull()?.lessonNumber?.ordinal ?: 0
            val nextLessonNumber = LessonNumber.values().getOrElse(lastLessonNumberOrdinal + 1) {
                LessonNumber.N5
            }
            items + ScheduleItemState.Empty.copy(
                group = items.first().group,
                dayNumber = items.first().dayNumber,
                lessonNumber = nextLessonNumber
            )
        }
    }

    fun removeScheduleItem(index: Long) {
        _scheduleItemsStates.update { items ->
            if (items.size <= 1) return@update items
            items.filterIndexed { itemIndex, _ -> itemIndex.toLong() != index }
        }
    }

    fun setGroup(group: PagingDropDownMenuState<Group>) {
        _scheduleItemsStates.update { items ->
            items.map {
                it.copy(group = group)
            }
        }
    }

    fun setDayNumber(dayNumber: DayNumber) {
        _scheduleItemsStates.update { items ->
            items.map {
                it.copy(dayNumber = dayNumber)
            }
        }
    }

    fun setClassroom(index: Long, classroom: PagingDropDownMenuState<Classroom>) {
        _scheduleItemsStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(classroom = classroom)
            }
        }
    }

    fun setTeacher(index: Long, teacher: PagingDropDownMenuState<Teacher>) {
        _scheduleItemsStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(teacher = teacher)
            }
        }
    }

    fun setEventName(index: Long, eventName: String) {
        _scheduleItemsStates.update { items ->
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
        _scheduleItemsStates.update { items ->
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
        _scheduleItemsStates.update { items ->
            items.mapIndexed { itemIndex, item ->
                if (itemIndex.toLong() != index) return@mapIndexed item
                item.copy(lessonNumber = lessonNumber)
            }
        }
    }

    fun setWeekAlternation(index: Long, weekAlternation: WeekAlternation) {
        _scheduleItemsStates.update { items ->
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