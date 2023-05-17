package tables.presentation.screen.classrooms

import androidx.compose.runtime.*
import app.cash.paging.LoadStateLoading
import coreui.compose.*
import coreui.theme.AppTheme
import coreui.util.CollectUiEvents
import coreui.util.UiMessage
import coreui.util.collectAsLazyPagingItems
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.marginTop
import org.jetbrains.compose.web.css.px
import tables.presentation.compose.InteractiveTable
import tables.presentation.compose.InteractiveTableBodyRow
import tables.presentation.compose.InteractiveTableHeaderRow

@Composable
fun Classrooms() {
    val viewModel: ClassroomsViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val classrooms = viewModel.pagedClassrooms.collectAsLazyPagingItems()

    val isListAppending by remember {
        derivedStateOf {
            classrooms.loadState.append == LoadStateLoading
        }
    }

    val isListRefreshing by remember {
        derivedStateOf {
            classrooms.loadState.refresh == LoadStateLoading
        }
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

    InteractiveTable(
        attrs = {
            style {
                marginTop(16.px)
                marginBottom(16.px)
                height(500.px)
                overflowY(Overflow.Auto)
            }
        },
        isRefreshing = isListRefreshing,
        isAppending = isListAppending,
        header = {
            InteractiveTableHeaderRow {
                TableHeaderItem {
                    Text(text = AppTheme.stringResources.navigationClassrooms)
                }
            }
        },
        body = {
            (0 until classrooms.itemCount).forEach { index ->
                val item = classrooms[index]

                InteractiveTableBodyRow(
                    onEdit = {
                        println("EDIT $index")
                    },
                    onSelectionChanged = {
                        println("SELECTED $it $index")
                    },
                    isSelected = false
                ) {
                    TableBodyItem {
                        Text(text = item?.name.toString())
                    }
                }
            }
        }
    )
}