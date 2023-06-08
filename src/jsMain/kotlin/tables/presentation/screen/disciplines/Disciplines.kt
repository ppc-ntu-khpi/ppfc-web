/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.disciplines

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
import tables.presentation.compose.item
import tables.presentation.compose.row

@Composable
fun Disciplines() {
    val viewModel: DisciplinesViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedDisciplines = viewModel.pagedDisciplines.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedDisciplines.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is DisciplinesViewEvent.Message -> uiMessage = event.message
                is DisciplinesViewEvent.DisciplineSaved -> viewModel.dialog(dialog = null)
                is DisciplinesViewEvent.DisciplineDeleted -> viewModel.dialog(dialog = null)
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
            is DisciplinesDialog.ManageDiscipline -> {
                ManageDisciplineDialog(
                    isLoading = viewState.isSaving,
                    discipline = dialog.discipline,
                    onSave = { discipline ->
                        viewModel.saveDiscipline(discipline = discipline)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is DisciplinesDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteDisciplines()
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
                        dialog = DisciplinesDialog.ManageDiscipline(discipline = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = DisciplinesDialog.ConfirmDeletion(
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
                label = AppTheme.stringResources.disciplinesSearchLabel
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
            lazyPagingItems = pagedDisciplines,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.disciplinesName)
                    }
                }
            },
            body = { _, item ->
                row(
                    isSelected = viewState.rowsSelection[item.id] ?: false,
                    onSelectionChanged = { isSelected ->
                        viewModel.setRowSelection(id = item.id, isSelected = isSelected)
                    },
                    onEdit = {
                        viewModel.dialog(dialog = DisciplinesDialog.ManageDiscipline(discipline = item))
                    }
                ) {
                    item {
                        Text(text = item.name)
                    }
                }
            }
        )
    }
}