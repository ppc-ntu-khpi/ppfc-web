/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.model.Discipline
import tables.domain.observer.ObservePagedDisciplines
import tables.presentation.compose.PagingDropDownMenuState
import tables.presentation.screen.teachers.model.TeacherState

class ManageTeacherViewModel(
    private val observePagedDisciplines: ObservePagedDisciplines
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _teacherState = MutableStateFlow(TeacherState.Empty)
    private val isFormBlank = _teacherState.map { teacherState ->
        teacherState.firstName.text.isBlank()
                || teacherState.lastName.text.isBlank()
                || teacherState.middleName.text.isBlank()
                || teacherState.disciplinesMenu.selectedItem == null
    }

    val pagedDisciplines: Flow<PagingData<Discipline>> =
        observePagedDisciplines.flow.cachedIn(coroutineScope)

    val state: StateFlow<ManageTeacherViewState> = combine(
        _teacherState,
        isFormBlank
    ) { teacherState, isFormBlank ->
        ManageTeacherViewState(
            teacherState = teacherState,
            isFormBlank = isFormBlank
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ManageTeacherViewState.Empty,
    )

    init {
        _teacherState.map { it.disciplinesMenu.searchQuery }.distinctUntilChanged().onEach { searchQuery ->
            observePagedDisciplines(searchQuery = searchQuery)
        }.launchIn(coroutineScope)
    }

    private fun observePagedDisciplines(
        searchQuery: String? = null
    ) {
        observePagedDisciplines(
            params = ObservePagedDisciplines.Params(
                searchQuery = searchQuery,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    fun loadTeacherState(teacherState: TeacherState) {
        _teacherState.value = teacherState
    }

    fun setFirstName(name: String) {
        _teacherState.update {
            it.copy(
                firstName = it.firstName.copy(
                    text = name,
                    error = null
                ),
            )
        }
    }

    fun setLastName(name: String) {
        _teacherState.update {
            it.copy(
                lastName = it.lastName.copy(
                    text = name,
                    error = null
                ),
            )
        }
    }

    fun setMiddleName(name: String) {
        _teacherState.update {
            it.copy(
                middleName = it.middleName.copy(
                    text = name,
                    error = null
                ),
            )
        }
    }

    fun setIsHeadTeacher(isHeadTeacher: Boolean) {
        _teacherState.update {
            it.copy(isHeadTeacher = isHeadTeacher)
        }
    }

    fun setDiscipline(disciplinesMenu: PagingDropDownMenuState<Discipline>) {
        _teacherState.update {
            it.copy(disciplinesMenu = disciplinesMenu)
        }
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20
        )
    }
}