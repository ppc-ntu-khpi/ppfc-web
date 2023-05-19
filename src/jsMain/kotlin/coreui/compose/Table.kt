package coreui.compose

import androidx.compose.runtime.Composable
import coreui.theme.AppStyleSheet.style
import coreui.theme.AppTheme
import coreui.theme.Typography
import coreui.util.alpha
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.HTMLTableCellElement
import org.w3c.dom.HTMLTableElement
import org.w3c.dom.HTMLTableRowElement

@Composable
fun Table(
    attrs: AttrBuilderContext<HTMLTableElement>? = null,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit
) {
    Table(
        attrs = {
            style {
                width(LengthKeyword.Auto)
                borderCollapse(BorderCollapse.Collapse)
            }

            "tbody tr:nth-of-type(odd)" style {
                backgroundColor(AppTheme.colors.primary.alpha(0.1f))
            }

            applyAttrs(attrs)
        }
    ) {
        Thead {
            header()
        }

        Tbody {
            body()
        }
    }
}

@Composable
fun TableRow(
    attrs: AttrBuilderContext<HTMLTableRowElement>? = null,
    content: @Composable () -> Unit
) {
    Tr(
        attrs = {
            applyAttrs(attrs)
        }
    ) {
        content()
    }
}

@Composable
fun TableHeaderItem(
    attrs: AttrBuilderContext<HTMLTableCellElement>? = null,
    content: @Composable () -> Unit
) {
    Th(
        attrs = {
            style {
                width(1.percent)
                paddingTop(10.px)
                paddingBottom(10.px)
                paddingLeft(16.px)
                paddingRight(16.px)
                backgroundColor(AppTheme.colors.primary)
                color(AppTheme.colors.onPrimary)
                fontWeight(FontWeight.Bold)
                fontSize(Typography.titleMedium)
            }

            applyAttrs(attrs)
        }
    ) {
        content()
    }
}

@Composable
fun TableBodyItem(
    attrs: AttrBuilderContext<HTMLTableCellElement>? = null,
    content: @Composable () -> Unit
) {
    Td(
        attrs = {
            style {
                width(1.percent)
                paddingTop(10.px)
                paddingBottom(10.px)
                paddingLeft(16.px)
                paddingRight(16.px)
                color(AppTheme.colors.onSurface)
            }

            applyAttrs(attrs)
        }
    ) {
        content()
    }
}