/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.schedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.*
import coreui.compose.base.*
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.collectAsLazyPagingItems
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import tables.domain.model.DayNumber
import tables.domain.model.LessonNumber
import tables.domain.model.ScheduleItem
import tables.domain.model.WeekAlternation
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.compose.PagingDropDownMenu
import tables.presentation.screen.schedule.mapper.toDomain
import tables.presentation.screen.schedule.mapper.toState

@Composable
fun EditScheduleItemDialog(
    isLoading: Boolean,
    scheduleItem: ScheduleItem,
    onSave: (scheduleItem: ScheduleItem) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: EditScheduleItemViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val pagedGroups = viewModel.pagedGroups.collectAsLazyPagingItems()
    val pagedClassrooms = viewModel.pagedClassrooms.collectAsLazyPagingItems()
    val pagedTeachers = viewModel.pagedTeachers.collectAsLazyPagingItems()
    val pagedSubjects = viewModel.pagedSubjects.collectAsLazyPagingItems()

    LaunchedEffect(scheduleItem) {
        viewModel.loadScheduleItemState(scheduleItemState = scheduleItem.toState())
    }

    Column(
        attrs = {
            style {
                margin(20.px)
            }
        }
    ) {
        Text(
            text = AppTheme.stringResources.tableDialogEditTitle,
            fontSize = Typography.headlineSmall
        )

        Spacer(height = 16.px)

        Row(
            horizontalArrangement = Arrangement.Horizontal.SpaceBetween
        ) {
            Column {
                PagingDropDownMenu(
                    lazyPagingItems = pagedGroups,
                    state = viewState.scheduleItemState.group,
                    label = AppTheme.stringResources.scheduleGroupNumber,
                    itemLabel = { item ->
                        item.number.toString()
                    }
                ) { state ->
                    viewModel.setGroup(group = state)
                }

                Spacer(height = 16.px)

                PagingDropDownMenu(
                    lazyPagingItems = pagedClassrooms,
                    state = viewState.scheduleItemState.classroom,
                    label = AppTheme.stringResources.scheduleClassroomName,
                    itemLabel = { item ->
                        item.name
                    }
                ) { state ->
                    viewModel.setClassroom(classroom = state)
                }

                Spacer(height = 16.px)

                PagingDropDownMenu(
                    lazyPagingItems = pagedSubjects,
                    state = viewState.scheduleItemState.subject,
                    label = AppTheme.stringResources.scheduleSubject,
                    itemLabel = { item ->
                        item.name
                    }
                ) { state ->
                    viewModel.setSubject(subject = state)
                }

                Spacer(height = 16.px)

                OutlinedTextField(
                    value = viewState.scheduleItemState.eventName.text,
                    label = AppTheme.stringResources.scheduleEventName
                ) { text ->
                    viewModel.setEventName(eventName = text)
                }

                Spacer(height = 16.px)

                PagingDropDownMenu(
                    lazyPagingItems = pagedTeachers,
                    state = viewState.scheduleItemState.teacher,
                    label = AppTheme.stringResources.scheduleTeacher,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { state ->
                    viewModel.setTeacher(teacher = state)
                }
            }

            Spacer(width = 16.px)

            Column {
                DropDownMenu(
                    items = DayNumber.values().toList(),
                    selectedItem = viewState.scheduleItemState.dayNumber,
                    label = AppTheme.stringResources.scheduleDayNumber,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setDayNumber(dayNumber = item)
                }

                Spacer(height = 16.px)

                DropDownMenu(
                    items = LessonNumber.values().toList(),
                    selectedItem = viewState.scheduleItemState.lessonNumber,
                    label = AppTheme.stringResources.scheduleLessonNumber,
                    itemLabel = { item ->
                        item.number.toString()
                    }
                ) { item ->
                    viewModel.setLessonNumber(lessonNumber = item)
                }

                Spacer(height = 16.px)

                DropDownMenu(
                    items = WeekAlternation.values().toList(),
                    selectedItem = viewState.scheduleItemState.weekAlternation,
                    label = AppTheme.stringResources.scheduleWeekAlternation,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setWeekAlternation(weekAlternation = item)
                }
            }
        }

        Spacer(height = 16.px)

        Row(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalArrangement = Arrangement.Horizontal.Center
        ) {
            OutlinedButton(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
                onClick = {
                    onClose()
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogCancel)
            }

            Spacer(width = 24.px)

            ButtonWithLoader(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
                enabled = !(viewState.isFormBlank || isLoading),
                loader = isLoading,
                onClick = {
                    onSave(viewState.scheduleItemState.toDomain())
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogSave)
            }
        }
    }
}