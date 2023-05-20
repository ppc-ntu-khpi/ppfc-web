/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import androidx.compose.runtime.*
import coreui.compose.UiMessageHost
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
        onEvent = { event ->
            when (event) {
                is ClassroomsViewEvent.Message -> uiMessage = event.message
            }
        },
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    )

    UiMessageHost(message = uiMessage)

    InteractiveTable(
        attrs = {
            style {
                width(100.percent)
                marginTop(16.px)
                marginBottom(16.px)
            }
        },
        lazyPagingItems = classrooms,
        header = tableHeaderRow(AppTheme.stringResources.classroomsName),
        bodyItem = { item ->
            tableBodyRow(
                isSelected = true,
                onSelectionChanged = {

                },
                onEdit = {

                },
                item.name
            )
        },
    )
}