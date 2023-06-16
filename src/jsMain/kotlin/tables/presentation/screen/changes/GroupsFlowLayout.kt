/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.presentation.screen.changes

import androidx.compose.runtime.Composable
import coreui.compose.FontWeight
import coreui.compose.Text
import coreui.compose.base.Row
import coreui.theme.AppTheme
import coreui.theme.Shape
import coreui.theme.Typography
import org.jetbrains.compose.web.css.*
import tables.domain.model.Group

private val groupsFlowLayoutWidth = 240.px

@Composable
fun GroupsFlowLayout(
    groups: List<Group>,
    onRemove: (group: Group) -> Unit
) {
    Row(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                flexWrap(FlexWrap.Wrap)
                width(groupsFlowLayoutWidth)
                maxWidth(groupsFlowLayoutWidth)
            }
        }
    ) {
        groups.forEach { group ->
            Text(
                attrs = {
                    style {
                        backgroundColor(AppTheme.colors.primary)
                        color(AppTheme.colors.onPrimary)
                        padding(5.px)
                        margin(4.px)
                        borderRadius(Shape.large)
                    }

                    onClick {
                        onRemove(group)
                    }
                },
                text = group.number.toString(),
                fontSize = Typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (groups.isNotEmpty()) return@Row

        Text(text = AppTheme.stringResources.createChangesGroupIsNotSelected)
    }
}