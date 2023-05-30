/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

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
import tables.presentation.compose.*

@Composable
fun Schedule() {
    val viewModel: ScheduleViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    var uiMessage by remember { mutableStateOf<UiMessage?>(null) }
    val pagedGroups = viewModel.pagedGroups.collectAsLazyPagingItems()
    val pagedTeachers = viewModel.pagedTeachers.collectAsLazyPagingItems()
    val pagedSchedule = viewModel.pagedSchedule.collectAsLazyPagingItems()
    val selectedRowsNumber = viewState.rowsSelection.count { it.value }.toLong()

    CollectPagingError(combinedLoadStates = pagedSchedule.loadState) { cause ->
        viewModel.handlePagingError(cause = cause)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is ScheduleViewEvent.Message -> uiMessage = event.message
                is ScheduleViewEvent.ScheduleItemSaved -> viewModel.dialog(dialog = null)
                is ScheduleViewEvent.ScheduleItemDeleted -> viewModel.dialog(dialog = null)
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
            /*
            is ScheduleDialog.ManageScheduleItem -> {
                ManageTeacherDialog(
                    isLoading = viewState.isSaving,
                    teacher = dialog.teacher,
                    onSave = { teacher ->
                        viewModel.saveTeacher(teacher = teacher)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }
             */

            is ScheduleDialog.ConfirmDeletion -> {
                ConfirmDeletionDialog(
                    isLoading = viewState.isDeleting,
                    itemsNumber = dialog.itemsNumber,
                    onConfirm = {
                        viewModel.deleteScheduleItems()
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            else -> {}
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
                        dialog = ScheduleDialog.ManageScheduleItem(scheduleItem = null)
                    )
                }
            ) {
                Text(text = AppTheme.stringResources.tableAdd)
            }

            Spacer(width = 10.px)

            OutlinedButton(
                onClick = {
                    viewModel.dialog(
                        dialog = ScheduleDialog.ConfirmDeletion(
                            itemsNumber = selectedRowsNumber
                        )
                    )
                },
                enabled = selectedRowsNumber > 0
            ) {
                Text(text = AppTheme.stringResources.tableDelete)
            }

            Spacer(width = 10.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedGroups,
                state = viewState.filterGroup,
                label = AppTheme.stringResources.scheduleFilterByGroupLabel,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { state ->
                viewModel.setFilterGroup(filterGroup = state)
            }

            Spacer(width = 10.px)

            PagingDropDownMenu(
                lazyPagingItems = pagedTeachers,
                state = viewState.filterTeacher,
                label = AppTheme.stringResources.scheduleFilterByTeacherLabel,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { state ->
                viewModel.setFilterTeacher(filterTeacher = state)
            }
        }

        Spacer(height = 16.px)

        PagingTable(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            lazyPagingItems = pagedSchedule,
            header = tableHeaderRow(
                AppTheme.stringResources.scheduleGroupNumber,
                AppTheme.stringResources.scheduleClassroomName,
                AppTheme.stringResources.scheduleTeacher,
                AppTheme.stringResources.scheduleSubjectOrEventName,
                AppTheme.stringResources.scheduleLessonNumber,
                AppTheme.stringResources.scheduleDayNumber,
                AppTheme.stringResources.scheduleIsNumerator
            ),
            bodyItem = { item ->
                tableBodyRow(
                    isSelected = viewState.rowsSelection[item.id] ?: false,
                    onSelectionChanged = { isSelected ->
                        viewModel.setRowSelection(id = item.id, isSelected = isSelected)
                    },
                    onEdit = {
                        viewModel.dialog(dialog = ScheduleDialog.ManageScheduleItem(scheduleItem = item))
                    },
                    item.group.number.toString(),
                    item.classroom.name,
                    item.teacher.toTextRepresentation(),
                    item.eventName ?: item.subject.name,
                    item.lessonNumber.number.toString(),
                    item.dayNumber.toTextRepresentation(),
                    if(item.isNumerator) {
                        AppTheme.stringResources.numerator
                    } else {
                        AppTheme.stringResources.denominator
                    }
                )
            }
        )
    }
}