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
import tables.extensions.onItem
import tables.extensions.onSearchQuery
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.schedule.mapper.toDomain
import tables.presentation.screen.schedule.model.ScheduleCommonLessonState
import tables.presentation.screen.schedule.model.ScheduleLessonState

class CreateScheduleItemsViewModel(
    private val observePagedGroups: ObservePagedGroups,
    private val observePagedClassrooms: ObservePagedClassrooms,
    private val observePagedTeachers: ObservePagedTeachers,
    private val observePagedSubjects: ObservePagedSubjects
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _scheduleCommonLesson = MutableStateFlow(ScheduleCommonLessonState.Empty)
    private val _scheduleLessons = MutableStateFlow(
        mapOf(Id.random() to ScheduleLessonState.Empty)
    )
    private val isFormBlank = combine(
        _scheduleCommonLesson,
        _scheduleLessons
    ) { scheduleCommonLesson, scheduleLessons ->
        isScheduleCommonLessonNotValid(scheduleCommonLesson = scheduleCommonLesson)
                || scheduleLessons.values.any { scheduleLesson ->
            isScheduleLessonNotValid(scheduleLesson = scheduleLesson)
        }
    }
    private val canAddItems = _scheduleLessons.map { scheduleLessons ->
        scheduleLessons.size < LessonNumber.values().size * 2
    }
    private val canRemoveItems = _scheduleLessons.map { scheduleLessons ->
        scheduleLessons.size > 1
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
        _scheduleCommonLesson,
        _scheduleLessons,
        isFormBlank,
        canAddItems,
        canRemoveItems
    ) { scheduleCommonLesson, scheduleLessons, isFormBlank, canAddItems, canRemoveItems ->
        CreateScheduleItemsViewState(
            scheduleCommonLesson = scheduleCommonLesson,
            scheduleLessons = scheduleLessons,
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
        observePagedGroups()
        observePagedClassrooms()
        observePagedTeachers()
        observePagedSubjects()

        val pagingGroups = _scheduleCommonLesson.map { it.group }
        pagingGroups.onSearchQuery { searchQuery ->
            observePagedGroups(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        val pagingClassrooms = _scheduleLessons.map { items -> items.values.map { it.classroom } }
        pagingClassrooms.onSearchQuery { searchQuery ->
            observePagedClassrooms(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
        pagingClassrooms.onItem { observePagedClassrooms() }.launchIn(coroutineScope)

        val pagingTeachers = _scheduleLessons.map { items -> items.values.map { it.teacher } }
        pagingTeachers.onSearchQuery { searchQuery ->
            observePagedTeachers(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
        pagingTeachers.onItem { observePagedTeachers() }.launchIn(coroutineScope)

        val pagingSubjects = _scheduleLessons.map { items -> items.values.map { it.subject } }
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

    private fun isScheduleCommonLessonNotValid(scheduleCommonLesson: ScheduleCommonLessonState): Boolean {
        return scheduleCommonLesson.group.selectedItem == null
    }

    private fun isScheduleLessonNotValid(scheduleLesson: ScheduleLessonState): Boolean {
        return scheduleLesson.classroom.selectedItem == null
                || scheduleLesson.teacher.selectedItem == null
                || (scheduleLesson.subject.selectedItem == null
                && scheduleLesson.eventName == TextFieldState.Empty)
    }

    fun getScheduleItems(): List<ScheduleItem>? {
        val scheduleCommonLesson = _scheduleCommonLesson.value
        val scheduleLessons = _scheduleLessons.value

        if (isScheduleCommonLessonNotValid(scheduleCommonLesson = scheduleCommonLesson)) return null
        return scheduleLessons.values.map { item ->
            if (isScheduleLessonNotValid(scheduleLesson = item)) return null
            item.toDomain(
                group = scheduleCommonLesson.group.selectedItem!!,
                dayNumber = scheduleCommonLesson.dayNumber
            )
        }
    }

    fun addScheduleItem() {
        _scheduleLessons.update { items ->
            if (items.size >= LessonNumber.values().size * 2) return@update items
            val lastLessonNumberOrdinal = items.values.lastOrNull()?.lessonNumber?.ordinal ?: 0
            val nextLessonNumber = LessonNumber.values().getOrElse(lastLessonNumberOrdinal + 1) {
                LessonNumber.N5
            }
            items + (Id.random() to ScheduleLessonState.Empty.copy(lessonNumber = nextLessonNumber))
        }
    }

    fun removeScheduleItem(id: Id.Value) {
        _scheduleLessons.update { items ->
            if (items.size <= 1) return@update items
            items.filterNot { item ->
                item.key == id
            }
        }
    }

    fun setGroup(group: PagingDropDownMenuState<Group>) {
        _scheduleCommonLesson.update { item ->
            item.copy(group = group)
        }
    }

    fun setDayNumber(dayNumber: DayNumber) {
        _scheduleCommonLesson.update { item ->
            item.copy(dayNumber = dayNumber)
        }
    }

    fun setClassroom(id: Id.Value, classroom: PagingDropDownMenuState<Classroom>) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(classroom = classroom))
        }
    }

    fun setTeacher(id: Id.Value, teacher: PagingDropDownMenuState<Teacher>) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(teacher = teacher))
        }
    }

    fun setEventName(id: Id.Value, eventName: String) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(
                eventName = item.eventName.copy(text = eventName),
                subject = PagingDropDownMenuState.Empty()
            ))
        }
    }

    fun setSubject(id: Id.Value, subject: PagingDropDownMenuState<Subject>) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(
                subject = subject,
                eventName = TextFieldState.Empty
            ))
        }
    }

    fun setLessonNumber(id: Id.Value, lessonNumber: LessonNumber) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(lessonNumber = lessonNumber))
        }
    }

    fun setWeekAlternation(id: Id.Value, weekAlternation: WeekAlternation) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(weekAlternation = weekAlternation))
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}