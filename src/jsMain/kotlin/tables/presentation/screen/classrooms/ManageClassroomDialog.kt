/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.classrooms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coreui.compose.ButtonWithLoader
import coreui.compose.OutlinedButton
import coreui.compose.OutlinedTextField
import coreui.compose.Text
import coreui.compose.base.Alignment
import coreui.compose.base.Arrangement
import coreui.compose.base.Column
import coreui.compose.base.Spacer
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import tables.domain.model.Classroom
import tables.presentation.screen.classrooms.mapper.toDomain
import tables.presentation.screen.classrooms.mapper.toState

@Composable
fun ManageClassroomDialog(
    isLoading: Boolean,
    classroom: Classroom? = null,
    onSave: (classroom: Classroom) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: ManageClassroomViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(classroom) {
        classroom ?: return@LaunchedEffect
        viewModel.loadClassroomState(classroomState = classroom.toState())
    }

    Column(
        attrs = {
            style {
                width(250.px)
                margin(20.px)
            }
        }
    ) {
        Text(
            text = AppTheme.stringResources.tableDialogEditTitle,
            fontSize = Typography.headlineSmall
        )

        Spacer(height = 16.px)

        OutlinedTextField(
            value = viewState.classroomState.name.text,
            label = AppTheme.stringResources.classroomsName
        ) { text ->
            viewModel.setName(
                name = text
            )
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
                    onSave(viewState.classroomState.toDomain())
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogSave)
            }
        }
    }
}