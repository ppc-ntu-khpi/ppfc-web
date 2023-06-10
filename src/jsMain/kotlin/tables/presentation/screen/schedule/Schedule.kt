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
import tables.presentation.common.model.DayNumberOption
import tables.presentation.common.model.WeekAlternationOption
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
            is ScheduleDialog.CreateScheduleItems -> {
                CreateScheduleItemsDialog(
                    isLoading = viewState.isSaving,
                    onSave = { scheduleItems ->
                        viewModel.saveScheduleItems(scheduleItems = scheduleItems)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

            is ScheduleDialog.EditScheduleItem -> {
                EditScheduleItemDialog(
                    isLoading = viewState.isSaving,
                    scheduleItem = dialog.scheduleItem,
                    onSave = { scheduleItem ->
                        viewModel.saveScheduleItem(scheduleItem = scheduleItem)
                    },
                    onClose = {
                        viewModel.dialog(dialog = null)
                    }
                )
            }

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
                        dialog = ScheduleDialog.CreateScheduleItems
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

            Spacer(width = 10.px)

            DropDownMenu(
                items = DayNumberOption.values().toList(),
                selectedItem = viewState.filterDayNumber,
                label = AppTheme.stringResources.scheduleFilterByDayNumber,
                itemLabel = { item ->
                    item.toTextRepresentation()
                }
            ) { item ->
                viewModel.setFilterDayNumber(filterDayNumber = item)
            }

            Spacer(width = 10.px)

            DropDownMenu(
                items = WeekAlternationOption.values().toList(),
                selectedItem = viewState.filterWeekAlternation,
                label = AppTheme.stringResources.scheduleFilterByWeekAlternation,
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
            lazyPagingItems = pagedSchedule,
            header = {
                row {
                    item {
                        Text(text = AppTheme.stringResources.scheduleGroupNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.scheduleClassroomName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.scheduleSubjectOrEventName)
                    }

                    item {
                        Text(text = AppTheme.stringResources.scheduleTeacher)
                    }

                    item {
                        Text(text = AppTheme.stringResources.scheduleDayNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.scheduleLessonNumber)
                    }

                    item {
                        Text(text = AppTheme.stringResources.scheduleWeekAlternation)
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
                        viewModel.dialog(dialog = ScheduleDialog.EditScheduleItem(scheduleItem = item))
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
                        Text(text = item.dayNumber.toTextRepresentation())
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