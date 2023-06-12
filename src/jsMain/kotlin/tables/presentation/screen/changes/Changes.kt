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
import coreui.theme.AppTheme
import coreui.util.*
import org.jetbrains.compose.web.css.*
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.common.model.WeekAlternationOption
import tables.presentation.compose.*
import kotlin.js.Date

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
            is ChangesDialog.CreateChanges -> {
                /*
                CreatechangesItemsDialog(
                    isLoading = viewState.isSaving,
                    onSave = { change ->
                        viewModel.saveChange(change = change)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
                 */
            }

            is ChangesDialog.EditChange -> {
                /*
                EditchangesItemDialog(
                    isLoading = viewState.isSaving,
                    changes = dialog.change,
                    onSave = { changes ->
                        viewModel.saveChanges(changes = changes)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
                 */
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
        }
    }

    Date

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

            DatePicker(
                date = viewState.filterDate
            ) { date ->
                viewModel.setFilterDate(filterDate = date)
            }

            Spacer(width = 10.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedGroups,
                state = viewState.filterGroup,
                label = AppTheme.stringResources.changesFilterByGroupLabel,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { state ->
                viewModel.setFilterGroup(filterGroup = state)
            }

            Spacer(width = 10.px)

            DropDownMenu(
                items = WeekAlternationOption.values().toList(),
                selectedItem = viewState.filterWeekAlternation,
                label = AppTheme.stringResources.changesFilterByWeekAlternation,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { item ->
                viewModel.setFilterWeekAlternation(filterWeekAlternation = item)
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
                        Text(text = item.group.number.toString())
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
                        Text(text = item.lessonNumber.number.toString())
                    }

                    item {
                        Text(text = item.weekAlternation.toTextRepresentation())
                    }
                }
            }
        )
    }
}