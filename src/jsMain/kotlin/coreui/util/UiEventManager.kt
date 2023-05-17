package coreui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.random.Random

@Composable
fun <T> CollectUiEvents(
    event: UiEvent<T>?,
    onClear: (Long) -> Unit,
    onEvent: (T) -> Unit
) {
    LaunchedEffect(event?.id) {
        event?.let { event ->
            onEvent(event.event)
            onClear(event.id)
        }
    }
}

data class UiEvent<T>(
    val event: T,
    val id: Long = Random.Default.nextLong()
)

class UiEventManager<T> {
    private val _events = MutableStateFlow(emptyList<UiEvent<T>>())

    val event: Flow<UiEvent<T>?> = _events.map { it.firstOrNull() }.distinctUntilChanged()

    fun emitEvent(event: UiEvent<T>) {
        synchronized(this) {
            _events.value = _events.value + event
        }

    }

    fun clearEvent(id: Long) {
        synchronized(this) {
            _events.value = _events.value.filterNot { it.id == id }
        }
    }

    fun clearEvents() {
        synchronized(this) {
            _events.value = emptyList()
        }
    }
}