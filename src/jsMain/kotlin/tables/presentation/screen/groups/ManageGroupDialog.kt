/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.groups

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.ButtonWithLoader
import coreui.compose.OutlinedButton
import coreui.compose.OutlinedNumberField
import coreui.compose.Text
import coreui.compose.base.Alignment
import coreui.compose.base.Arrangement
import coreui.compose.base.Column
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.collectAsLazyPagingItems
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import tables.domain.model.Group
import tables.presentation.compose.PagingDropDownMenu
import tables.presentation.screen.groups.mapper.toDomain
import tables.presentation.screen.groups.mapper.toState

@Composable
fun ManageGroupDialog(
    isLoading: Boolean,
    group: Group? = null,
    onSave: (group: Group) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: ManageGroupViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()
    val pagedDisciplines = viewModel.pagedCourses.collectAsLazyPagingItems()

    LaunchedEffect(group) {
        group ?: return@LaunchedEffect
        viewModel.loadGroupState(groupState = group.toState())
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

        OutlinedNumberField(
            value = viewState.groupState.number.number,
            label = AppTheme.stringResources.groupsNumber
        ) { number ->
            viewModel.setNumber(number = number)
        }

        Spacer(height = 16.px)

        PagingDropDownMenu(
            lazyPagingItems = pagedDisciplines,
            state = viewState.groupState.course,
            label = AppTheme.stringResources.groupsFilterByCourseLabel,
            
            itemLabel = { item ->
                item.number.toString()
            }
        ) { state ->
            viewModel.setCourse(course = state)
        }

        Spacer(height = 16.px)

        Column(
            attrs = {
                style {
                    width(100.percent)
                }
            },
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalArrangement = Arrangement.Vertical.Center
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

            Spacer(height = 16.px)

            ButtonWithLoader(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
                enabled = !(viewState.isFormBlank || isLoading),
                loader = isLoading,
                onClick = {
                    onSave(viewState.groupState.toDomain())
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogSave)
            }
        }
    }
}