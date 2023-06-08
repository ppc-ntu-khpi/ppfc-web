/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.subjects

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
fun Subjects() {
    val viewModel: SubjectsViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedSubjects = viewModel.pagedSubjects.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedSubjects.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is SubjectsViewEvent.Message -> uiMessage = event.message
                is SubjectsViewEvent.SubjectSaved -> viewModel.dialog(dialog = null)
                is SubjectsViewEvent.SubjectDeleted -> viewModel.dialog(dialog = null)
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
            is SubjectsDialog.ManageSubject -> {
                ManageSubjectDialog(
                    isLoading = viewState.isSaving,
                    subject = dialog.subject,
                    onSave = { subject ->
                        viewModel.saveSubject(subject = subject)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is SubjectsDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteSubjects()
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
                        dialog = SubjectsDialog.ManageSubject(subject = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = SubjectsDialog.ConfirmDeletion(
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
                label = AppTheme.stringResources.subjectsSearchLabel
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
            lazyPagingItems = pagedSubjects,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.subjectsName)
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
                        viewModel.dialog(dialog = SubjectsDialog.ManageSubject(subject = item))
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