/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tables.presentation.screen.disciplines.model.DisciplineState

class ManageDisciplineViewModel {

    private val _disciplineState = MutableStateFlow(DisciplineState.Empty)
    private val _isFormBlank = _disciplineState.map { disciplineState ->
        disciplineState.name.text.isBlank()
    }

    val state: StateFlow<ManageDisciplineViewState> = combine(
        _disciplineState,
        _isFormBlank
    ) { disciplineState, isFormBlank ->
        ManageDisciplineViewState(
            disciplineState = disciplineState,
            isFormBlank = isFormBlank
        )
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ManageDisciplineViewState.Empty,
    )

    fun loadDisciplineState(disciplineState: DisciplineState) {
        _disciplineState.value = disciplineState
    }

    fun setName(name: String) {
        _disciplineState.update {
            it.copy(
                name = it.name.copy(
                    text = name,
                    error = null
                ),
            )
        }
    }
}