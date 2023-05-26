/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.util.*
import org.jetbrains.compose.web.css.*
import tables.presentation.compose.ConfirmDeletionDialog
import tables.presentation.compose.PagingTable
import tables.presentation.compose.tableBodyRow
import tables.presentation.compose.tableHeaderRow

@Composable
fun Classrooms() {
    val viewModel: ClassroomsViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val classrooms = viewModel.pagedClassrooms.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = classrooms.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is ClassroomsViewEvent.Message -> uiMessage = event.message
                is ClassroomsViewEvent.ClassroomSaved -> viewModel.dialog(dialog = null)
                is ClassroomsViewEvent.ClassroomDeleted -> viewModel.dialog(dialog = null)
            }
        },
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    )

    UiMessageHost(message = uiMessage)

    DialogHost(
        dialog = viewState.dialog
    ) { dialog ->
        when (dialog) {
            is ClassroomsDialog.ManageClassroom -> {
                ManageClassroomDialog(
                    isLoading = viewState.isSaving,
                    classroom = dialog.classroom,
                    onSave = { classroomState ->
                        viewModel.saveClassroom(classroomState = classroomState)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is ClassroomsDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteClassrooms()
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }
        }
    }

    Column(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
                paddingTop(16.px)
                paddingBottom(16.px)
            }
        }
    ) {
        Row(
            attrs = {
                style {
                    height(50.px)
                }
            },
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Button(
                onClick = {
                    viewModel.dialog(
                        dialog = ClassroomsDialog.ManageClassroom(classroom = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = ClassroomsDialog.ConfirmDeletion(
                            itemsNumber = selectedRowsNumber
                        )
                    )
                },
                enabled = selectedRowsNumber > 0
            ) {
                Text(text = AppTheme.stringResources.tableDelete)
            }

            Spacer(width = 10.px)

            OutlinedTextField(
                value = viewState.searchQuery.text,
                label = AppTheme.stringResources.classroomsSearchLabel,
                symmetricLayout = true
            ) { text ->
                viewModel.setSearchQuery(searchQuery = text)
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = classrooms,
            header = tableHeaderRow(AppTheme.stringResources.classroomsName),
            bodyItem = { item ->
                tableBodyRow(
                    isSelected = viewState.rowsSelection[item.id] ?: false,
                    onSelectionChanged = { isSelected ->
                        viewModel.setRowSelection(id = item.id, isSelected = isSelected)
                    },
                    onEdit = {
                        viewModel.dialog(dialog = ClassroomsDialog.ManageClassroom(classroom = item))
                    },
                    item.name
                )
            }
        )
    }
}