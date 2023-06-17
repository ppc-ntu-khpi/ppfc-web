/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

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
import tables.domain.model.Change
import tables.domain.model.DayNumber
import tables.domain.model.WeekAlternation
import tables.presentation.common.mapper.toTextRepresentation
import tables.presentation.compose.PagingDropDownMenu
import tables.presentation.screen.changes.mapper.toDomain
import tables.presentation.screen.changes.mapper.toState
import tables.presentation.screen.changes.mapper.toTextRepresentation
import tables.presentation.screen.changes.model.ChangeLessonNumberOption

@Composable
fun EditChangeDialog(
    isLoading: Boolean,
    change: Change,
    onSave: (change: Change) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: EditChangeViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val pagedGroups = viewModel.pagedGroups.collectAsLazyPagingItems()
    val pagedClassrooms = viewModel.pagedClassrooms.collectAsLazyPagingItems()
    val pagedTeachers = viewModel.pagedTeachers.collectAsLazyPagingItems()
    val pagedSubjects = viewModel.pagedSubjects.collectAsLazyPagingItems()

    LaunchedEffect(change) {
        viewModel.loadChangeState(changeState = change.toState())
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
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) groupsRow@ {
            PagingDropDownMenu(
                lazyPagingItems = pagedGroups,
                state = viewState.changeState.groupsMenu,
                enabled = viewState.canAddGroups,
                label = AppTheme.stringResources.changesGroupNumber,
                itemLabel = { item ->
                    item.number.toString()
                }
            ) { state ->
                viewModel.setGroup(groupsMenu = state)
                if (state.selectedItem == null
                    || state.selectedItem == viewState.changeState.groupsMenu.selectedItem) return@PagingDropDownMenu
                viewModel.addGroup(group = state.selectedItem)
            }

            Spacer(width = 16.px)

            GroupsFlowLayout(groups = viewState.changeState.selectedGroups.toList()) { group ->
                viewModel.removeGroup(group = group)
            }
        }

        Spacer(height = 16.px)

        Row(
            horizontalArrangement = Arrangement.Horizontal.SpaceBetween
        ) {
            Column {
                PagingDropDownMenu(
                    lazyPagingItems = pagedClassrooms,
                    state = viewState.changeState.classroomsMenu,
                    label = AppTheme.stringResources.changesClassroomName,
                    itemLabel = { item ->
                        item.name
                    }
                ) { state ->
                    viewModel.setClassroom(classroomsMenu = state)
                }

                Spacer(height = 16.px)

                PagingDropDownMenu(
                    lazyPagingItems = pagedSubjects,
                    state = viewState.changeState.subjectsMenu,
                    label = AppTheme.stringResources.changesSubject,
                    itemLabel = { item ->
                        item.name
                    }
                ) { state ->
                    viewModel.setSubject(subjectsMenu = state)
                }

                Spacer(height = 16.px)

                OutlinedTextField(
                    value = viewState.changeState.eventName.text,
                    label = AppTheme.stringResources.changesEventName
                ) { text ->
                    viewModel.setEventName(eventName = text)
                }

                Spacer(height = 16.px)

                PagingDropDownMenu(
                    lazyPagingItems = pagedTeachers,
                    state = viewState.changeState.teachersMenu,
                    label = AppTheme.stringResources.changesTeacher,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { state ->
                    viewModel.setTeacher(teachersMenu = state)
                }
            }

            Spacer(width = 16.px)

            Column {
                DatePicker(
                    date = viewState.changeState.date
                ) { date ->
                    viewModel.setDate(date = date)
                }

                Spacer(height = 16.px)

                DropDownMenu(
                    items = DayNumber.values().toList(),
                    selectedItem = viewState.changeState.dayNumber,
                    label = AppTheme.stringResources.changesDayNumber,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setDayNumber(dayNumber = item)
                }

                Spacer(height = 16.px)

                DropDownMenu(
                    items = ChangeLessonNumberOption.values().toList(),
                    selectedItem = viewState.changeState.lessonNumber,
                    label = AppTheme.stringResources.changesLessonNumber,
                    itemLabel = { item ->
                        item.toTextRepresentation()
                    }
                ) { item ->
                    viewModel.setLessonNumber(lessonNumber = item)
                }

                Spacer(height = 16.px)

                DropDownMenu(
                    items = WeekAlternation.values().toList(),
                    selectedItem = viewState.changeState.weekAlternation,
                    label = AppTheme.stringResources.changesWeekAlternation,
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
                    onSave(viewState.changeState.toDomain())
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogSave)
            }
        }
    }
}