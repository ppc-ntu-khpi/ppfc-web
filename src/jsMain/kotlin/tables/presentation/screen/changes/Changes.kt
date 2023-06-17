/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.theme.AppIconClass
import coreui.theme.AppTheme
import coreui.util.*
import infrastructure.util.downloadFileUri
import org.jetbrains.compose.web.css.*
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.compose.*

@Composable
fun Changes() {
    val viewModel: ChangesViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedGroups = viewModel.pagedGroups.collectAsLazyPagingItems()
    val pagedChanges = viewModel.pagedChanges.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedChanges.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is ChangesViewEvent.Message -> uiMessage = event.message
                is ChangesViewEvent.ChangeSaved -> viewModel.dialog(dialog = null)
                is ChangesViewEvent.ChangeDeleted -> viewModel.dialog(dialog = null)
                is ChangesViewEvent.ChangesExported -> {
                    downloadFileUri(file = event.document)
                }
            }
        },
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    )

    FullscreenProgressIndicator(visible = viewState.isLoading)

    UiMessageHost(message = uiMessage)

    DialogHost(
        dialog = viewState.dialog
    ) { dialog ->
        when (dialog) {
            is ChangesDialog.CreateChanges -> {
                CreateChangesDialog(
                    isLoading = viewState.isSaving,
                    onSave = { changes ->
                        viewModel.saveChanges(changes = changes)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is ChangesDialog.EditChange -> {
                EditChangeDialog(
                    isLoading = viewState.isSaving,
                    change = dialog.change,
                    onSave = { change ->
                        viewModel.saveChange(change = change)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is ChangesDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteChanges()
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is ChangesDialog.ConfirmDeletionOfAll -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    onConfirm = {
                        viewModel.deleteAllChanges()
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
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Button(
                onClick = {
                    viewModel.dialog(
                        dialog = ChangesDialog.CreateChanges
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = ChangesDialog.ConfirmDeletion(
                            itemsNumber = selectedRowsNumber
                        )
                    )
                },
                enabled = selectedRowsNumber > 0
            ) {
                Text(text = AppTheme.stringResources.tableDelete)
            }

            Spacer(width = 10.px)

            IconButton(
                icon = AppIconClass.DeleteAll
            ) {
                viewModel.dialog(dialog = ChangesDialog.ConfirmDeletionOfAll)
            }

            Spacer(width = 10.px)

            DatePicker(
                date = viewState.filterDate
            ) { date ->
                viewModel.setFilterDate(filterDate = date)
            }

            Spacer(width = 10.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedGroups,
                state = viewState.filterGroupsMenu,
                label = AppTheme.stringResources.changesFilterByGroupLabel,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { state ->
                viewModel.setFilterGroup(filterGroupsMenu = state)
            }

            Spacer(width = 10.px)

            IconButton(
                icon = AppIconClass.Export
            ) {
                viewModel.exportChangesToDocument()
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = pagedChanges,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.changesGroupNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.changesClassroomName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.changesSubjectOrEventName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.changesTeacher)
                    }

                    item {
                        Text(text = AppTheme.stringResources.changesLessonNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.changesDayNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.changesWeekAlternation)
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
                        viewModel.dialog(dialog = ChangesDialog.EditChange(change = item))
                    }
                ) {
                    item {
                        Text(text = item.groups.joinToString(separator = ",") { it.number.toString() })
                    }

                    item {
                        Text(text = item.classroom.name)
                    }

                    item {
                        Text(text = item.eventName ?: item.subject.name)
                    }

                    item {
                        Text(text = item.teacher.toTextRepresentation())
                    }

                    item {
                        Text(text = item.lessonNumber?.number?.toString() ?: "")
                    }

                    item {
                        Text(text = item.dayNumber.toTextRepresentation())
                    }

                    item {
                        Text(text = item.weekAlternation.toTextRepresentation())
                    }
                }
            }
        )
    }
}