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
import coreui.util.CollectUiEvents
import coreui.util.UiMessage
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import tables.domain.model.Classroom

@Composable
fun ManageClassroomDialog(
    classroom: Classroom? = null,
    onUiMessage: (uiMessage: UiMessage) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: ManageClassroomViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(classroom) {
        classroom ?: return@LaunchedEffect
        viewModel.loadClassroom(classroom = classroom)
    }

    CollectUiEvents(
        event = viewState.event,
        onEvent = { event ->
            when (event) {
                is ManageClassroomViewEvent.Message -> onUiMessage(event.message)
                is ManageClassroomViewEvent.ClassroomSaved -> onClose()
            }
        },
        onClear = { id ->
            viewModel.clearEvent(id = id)
        }
    )

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

        Spacer(height = 10.px)

        OutlinedTextField(
            value = viewState.classroomState.name.text,
            label = AppTheme.stringResources.classroomsName
        ) { text ->
            viewModel.setName(
                name = text
            )
        }

        Spacer(height = 18.px)

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
                Text("Скасувати")
            }

            Spacer(height = 16.px)

            ButtonWithLoader(
                attrs = {
                    style {
                        width(100.percent)
                    }
                },
                enabled = !(viewState.isFormBlank || viewState.isLoading),
                loader = viewState.isLoading,
                onClick = {
                    viewModel.saveClassroom()
                }
            ) {
                Text("Зберегти")
            }

        }
    }
}