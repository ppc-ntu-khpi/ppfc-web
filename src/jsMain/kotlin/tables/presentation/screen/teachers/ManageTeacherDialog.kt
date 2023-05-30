/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.teachers

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
import tables.domain.model.Teacher
import tables.presentation.compose.PagingDropDownMenu
import tables.presentation.screen.teachers.mapper.toDomain
import tables.presentation.screen.teachers.mapper.toState

@Composable
fun ManageTeacherDialog(
    isLoading: Boolean,
    teacher: Teacher? = null,
    onSave: (teacher: Teacher) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: ManageTeacherViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val pagedDisciplines = viewModel.pagedDisciplines.collectAsLazyPagingItems()

    LaunchedEffect(teacher) {
        teacher ?: return@LaunchedEffect
        viewModel.loadTeacherState(teacherState = teacher.toState())
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
                OutlinedTextField(
                    value = viewState.teacherState.lastName.text,
                    label = AppTheme.stringResources.teachersLastName
                ) { text ->
                    viewModel.setLastName(
                        name = text
                    )
                }

                Spacer(height = 16.px)

                OutlinedTextField(
                    value = viewState.teacherState.firstName.text,
                    label = AppTheme.stringResources.teachersFirstName
                ) { text ->
                    viewModel.setFirstName(
                        name = text
                    )
                }

                Spacer(height = 16.px)

                OutlinedTextField(
                    value = viewState.teacherState.middleName.text,
                    label = AppTheme.stringResources.teacherMiddleName
                ) { text ->
                    viewModel.setMiddleName(
                        name = text
                    )
                }
            }

            Spacer(width = 16.px)

            Column {
                PagingDropDownMenu(
                    lazyPagingItems = pagedDisciplines,
                    state = viewState.teacherState.discipline,
                    label = AppTheme.stringResources.teachersFilterByDisciplineLabel,
                    
                    itemLabel = { item ->
                        item.name
                    }
                ) { state ->
                    viewModel.setDiscipline(discipline = state)
                }

                Spacer(height = 16.px)

                CheckboxWithLabel(
                    checked = viewState.teacherState.isHeadTeacher,
                    label = AppTheme.stringResources.teachersIsHeadTeacher
                ) { checked ->
                    viewModel.setIsHeadTeacher(isHeadTeacher = checked)
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
                    onSave(viewState.teacherState.toDomain())
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogSave)
            }
        }
    }
}