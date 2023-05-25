/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import coreui.extensions.onSuccess
import coreui.extensions.withErrorMapper
import coreui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.domain.interactor.SaveClassroom
import tables.domain.model.Classroom
import tables.presentation.common.TableOperationErrorMapper
import tables.presentation.screen.classrooms.mapper.toDomain
import tables.presentation.screen.classrooms.mapper.toState
import tables.presentation.screen.classrooms.model.ClassroomState

class ManageClassroomViewModel(
    private val saveClassroom: SaveClassroom,
    private val tableOperationErrorMapper: TableOperationErrorMapper
) {

    private val loadingState = ObservableLoadingCounter()
    private val uiEventManager = UiEventManager<ManageClassroomViewEvent>()
    private val _classroomState = MutableStateFlow(ClassroomState.Empty)
    private val _isFormBlank = _classroomState.map { classroomState ->
        classroomState.name.text.isBlank()
    }

    val state: StateFlow<ManageClassroomViewState> = combine(
        _classroomState,
        _isFormBlank,
        loadingState.observable,
        uiEventManager.event
    ) { classroomState, isFormBlank, isLoading, event ->
        ManageClassroomViewState(
            classroomState = classroomState,
            isFormBlank = isFormBlank,
            isLoading = isLoading,
            event = event
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

    fun saveClassroom() = launchWithLoader(loadingState) {
        val classroom = _classroomState.value.toDomain().let {
            it.copy(
                name = it.name.trim()
            )
        }

        saveClassroom(
            params = SaveClassroom.Params(
                classroom = classroom
            )
        ).onSuccess {
            sendEvent(
                event = ManageClassroomViewEvent.ClassroomSaved
            )
        }.withErrorMapper(errorMapper = tableOperationErrorMapper) { message ->
            sendEvent(
                event = ManageClassroomViewEvent.Message(
                    message = UiMessage(message = message)
                )
            )
        }.collect()
    }

    private fun sendEvent(event: ManageClassroomViewEvent) {
        uiEventManager.emitEvent(
            event = UiEvent(
                event = event
            )
        )
    }

    fun clearEvent(id: Long) {
        uiEventManager.clearEvent(id = id)
    }
}