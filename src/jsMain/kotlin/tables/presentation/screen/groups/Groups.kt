/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import androidx.compose.runtime.*
import coreui.compose.*
import coreui.compose.base.Alignment
import coreui.compose.base.Column
import coreui.compose.base.Row
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.util.*
import org.jetbrains.compose.web.css.*
import tables.presentation.compose.*

@Composable
fun Groups() {
    val viewModel: GroupsViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedCourses = viewModel.pagedCourses.collectAsLazyPagingItems()
    val pagedGroups = viewModel.pagedGroups.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedGroups.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is GroupsViewEvent.Message -> uiMessage = event.message
                is GroupsViewEvent.GroupSaved -> viewModel.dialog(dialog = null)
                is GroupsViewEvent.GroupDeleted -> viewModel.dialog(dialog = null)
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
            is GroupsDialog.ManageGroup -> {
                ManageGroupDialog(
                    isLoading = viewState.isSaving,
                    group = dialog.group,
                    onSave = { group ->
                        viewModel.saveGroup(group = group)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is GroupsDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteGroups()
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
                        dialog = GroupsDialog.ManageGroup(group = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = GroupsDialog.ConfirmDeletion(
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
                label = AppTheme.stringResources.groupsSearchLabel
            ) { text ->
                viewModel.setSearchQuery(searchQuery = text)
            }

            Spacer(width = 10.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedCourses,
                state = viewState.filterCourse,
                label = AppTheme.stringResources.groupsFilterByCourseLabel,
                
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { state ->
                viewModel.setFilterCourse(filterCourse = state)
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = pagedGroups,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.groupsNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.groupsCourseNumber)
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
                        viewModel.dialog(dialog = GroupsDialog.ManageGroup(group = item))
                    }
                ) {
                    item {
                        Text(text = item.number.toString())
                    }

                    item {
                        Text(text = item.course.number.toString())
                    }
                }
            }
        )
    }
}