/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.courses

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
import coreui.util.rememberGet
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import tables.domain.model.Course
import tables.presentation.screen.courses.mapper.toDomain
import tables.presentation.screen.courses.mapper.toState

@Composable
fun ManageCourseDialog(
    isLoading: Boolean,
    course: Course? = null,
    onSave: (course: Course) -> Unit,
    onClose: () -> Unit
) {
    val viewModel: ManageCourseViewModel by rememberGet()
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(course) {
        course ?: return@LaunchedEffect
        viewModel.loadCourseState(courseState = course.toState())
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

        OutlinedNumberField(
            value = viewState.courseState.number.number,
            label = AppTheme.stringResources.coursesNumber
        ) { number ->
            viewModel.setNumber(
                number = number
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
                    onSave(viewState.courseState.toDomain())
                }
            ) {
                Text(text = AppTheme.stringResources.tableManageItemDialogSave)
            }
        }
    }
}