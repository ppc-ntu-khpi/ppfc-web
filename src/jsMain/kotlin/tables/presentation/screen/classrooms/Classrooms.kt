package tables.presentation.screen.classrooms

import androidx.compose.runtime.*
import coreui.compose.Overflow
import coreui.compose.UiMessageHost
import coreui.compose.overflowY
import coreui.theme.AppTheme
import coreui.util.*
import org.jetbrains.compose.web.css.*
import tables.presentation.compose.InteractiveTable
import tables.presentation.compose.tableBodyRow
import tables.presentation.compose.tableHeaderRow

@Composable
fun Classrooms() {
    val viewModel: ClassroomsViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val classrooms = viewModel.pagedClassrooms.collectAsLazyPagingItems()

    CollectPagingError(combinedLoadStates = classrooms.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    ) { event ->
        when (event) {
            is ClassroomsViewEvent.Message -> uiMessage = event.message
        }
    }

    UiMessageHost(message = uiMessage)

    InteractiveTable(
        attrs = {
            style {
                width(100.percent)
                marginTop(16.px)
                marginBottom(16.px)
                overflowY(Overflow.Scroll)
            }
        },
        lazyPagingItems = classrooms,
        header = tableHeaderRow(AppTheme.stringResources.classroomsName),
        bodyItem = { item ->
            tableBodyRow(
                isSelected = false,
                onSelectionChanged = {

                },
                onEdit = {

                },
                item.name
            )
        },
    )
}