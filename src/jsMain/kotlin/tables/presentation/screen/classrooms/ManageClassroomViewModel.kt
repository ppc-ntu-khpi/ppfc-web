/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.presentation.screen.classrooms.model.ClassroomState

class ManageClassroomViewModel {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

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
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ManageClassroomViewState.Empty,
    )

    fun loadClassroomState(classroomState: ClassroomState) {
        _classroomState.value = classroomState
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