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
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.schedule.mapper.toDomain
import tables.presentation.screen.schedule.model.ScheduleCommonLessonState
import tables.presentation.screen.schedule.model.ScheduleLessonNumberOption
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
    private val canAddLessons = _scheduleLessons.map { scheduleLessons ->
        scheduleLessons.size < ScheduleLessonNumberOption.values().size * 2
    }
    private val canRemoveLessons = _scheduleLessons.map { scheduleLessons ->
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

    private val _groupsSearchQuery = MutableStateFlow("")
    private val _classroomsSearchQuery = MutableStateFlow("")
    private val _teachersSearchQuery = MutableStateFlow("")
    private val _subjectsSearchQuery = MutableStateFlow("")

    val state: StateFlow<CreateScheduleItemsViewState> = combine(
        _scheduleCommonLesson,
        _scheduleLessons,
        isFormBlank,
        canAddLessons,
        canRemoveLessons
    ) { scheduleCommonLesson, scheduleLessons, isFormBlank, canAddItems, canRemoveItems ->
        CreateScheduleItemsViewState(
            scheduleCommonLesson = scheduleCommonLesson,
            scheduleLessons = scheduleLessons,
            isFormBlank = isFormBlank,
            canAddLessons = canAddItems,
            canRemoveLessons = canRemoveItems
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = CreateScheduleItemsViewState.Empty,
    )

    init {
        _groupsSearchQuery.onEach { searchQuery ->
            observePagedGroups(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _classroomsSearchQuery.onEach { searchQuery ->
            observePagedClassrooms(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _teachersSearchQuery.onEach { searchQuery ->
            observePagedTeachers(searchQuery = searchQuery)
        }.launchIn(coroutineScope)

        _subjectsSearchQuery.onEach { searchQuery ->
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

    private fun isScheduleCommonLessonNotValid(scheduleCommonLesson: ScheduleCommonLessonState): Boolean {
        return scheduleCommonLesson.groupsMenu.selectedItem == null
    }

    private fun isScheduleLessonNotValid(scheduleLesson: ScheduleLessonState): Boolean {
        return scheduleLesson.classroomsMenu.selectedItem == null
                || scheduleLesson.teachersMenu.selectedItem == null
                || (scheduleLesson.subjectsMenu.selectedItem == null
                && scheduleLesson.eventName == TextFieldState.Empty)
    }

    fun getScheduleItems(): List<ScheduleItem>? {
        val scheduleCommonLesson = _scheduleCommonLesson.value
        val scheduleLessons = _scheduleLessons.value

        if (isScheduleCommonLessonNotValid(scheduleCommonLesson = scheduleCommonLesson)) return null
        return scheduleLessons.values.map { item ->
            if (isScheduleLessonNotValid(scheduleLesson = item)) return null
            item.toDomain(
                group = scheduleCommonLesson.groupsMenu.selectedItem!!,
                dayNumber = scheduleCommonLesson.dayNumber
            )
        }
    }

    fun addScheduleItem() {
        _scheduleLessons.update { items ->
            if (items.size >= ScheduleLessonNumberOption.values().size * 2) return@update items
            val lastLessonNumberOrdinal = items.values.lastOrNull()?.lessonNumber?.ordinal ?: 0
            val nextLessonNumber = ScheduleLessonNumberOption.values().getOrElse(lastLessonNumberOrdinal + 1) {
                ScheduleLessonNumberOption.N5
            }
            items + (Id.random() to ScheduleLessonState.Empty.copy(lessonNumber = nextLessonNumber))
        }

        _groupsSearchQuery.value = ""
        _classroomsSearchQuery.value = ""
        _teachersSearchQuery.value = ""
        _subjectsSearchQuery.value = ""
    }

    fun removeScheduleItem(id: Id.Value) {
        _scheduleLessons.update { items ->
            if (items.size <= 1) return@update items
            items.filterNot { item ->
                item.key == id
            }
        }
    }

    fun setGroup(groupsMenu: PagingDropDownMenuState<Group>) {
        _scheduleCommonLesson.update { item ->
            item.copy(groupsMenu = groupsMenu)
        }
        _groupsSearchQuery.value = groupsMenu.searchQuery
    }

    fun setDayNumber(dayNumber: DayNumber) {
        _scheduleCommonLesson.update { item ->
            item.copy(dayNumber = dayNumber)
        }
    }

    fun setClassroom(id: Id.Value, classroomsMenu: PagingDropDownMenuState<Classroom>) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(classroomsMenu = classroomsMenu))
        }
        _classroomsSearchQuery.value = classroomsMenu.searchQuery
    }

    fun setTeacher(id: Id.Value, teachersMenu: PagingDropDownMenuState<Teacher>) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(teachersMenu = teachersMenu))
        }
        _teachersSearchQuery.value = teachersMenu.searchQuery
    }

    fun setEventName(id: Id.Value, eventName: String) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(
                eventName = item.eventName.copy(text = eventName),
                subjectsMenu = PagingDropDownMenuState.Empty()
            ))
        }
    }

    fun setSubject(id: Id.Value, subjectsMenu: PagingDropDownMenuState<Subject>) {
        _scheduleLessons.update { items ->
            val item = items[id] ?: return@update items
            items + (id to item.copy(
                subjectsMenu = subjectsMenu,
                eventName = TextFieldState.Empty
            ))
        }
        _subjectsSearchQuery.value = subjectsMenu.searchQuery
    }

    fun setLessonNumber(id: Id.Value, lessonNumber: ScheduleLessonNumberOption) {
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