/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.model.Classroom
import tables.presentation.screen.classrooms.mapper.toState
import tables.presentation.screen.classrooms.model.ClassroomState

class ManageClassroomViewModel {

    private val _classroomState = MutableStateFlow(ClassroomState.Empty)
    private val _isFormBlank = _classroomState.map { classroomState ->
        classroomState.name.text.isBlank()
    }

    val state: StateFlow<ManageClassroomViewState> = combine(
        _classroomState,
        _isFormBlank
    ) { classroomState, isFormBlank ->
        ManageClassroomViewState(
            classroomState = classroomState,
            isFormBlank = isFormBlank
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ManageClassroomViewState.Empty,
    )

    fun loadClassroom(classroom: Classroom) {
        _classroomState.value = classroom.toState()
    }

    fun setName(name: String) {
        _classroomState.update {
            it.copy(
                name = it.name.copy(
                    text = name,
                    error = null
                ),
            )
        }
    }
}