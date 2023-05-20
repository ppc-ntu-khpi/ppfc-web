/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.compose

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSLengthValue
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.StyleScope

fun StyleScope.boxShadow(
    offsetX: CSSLengthValue,
    offsetY: CSSLengthValue,
    blurRadius: CSSLengthValue,
    color: CSSColorValue
) {
    property(
        "box-shadow",
        "${offsetX.value}${offsetX.unit} ${offsetY.value}${offsetY.unit} ${blurRadius.value}${blurRadius.unit} $color"
    )
}

fun StyleScope.dropShadow(
    offsetX: CSSLengthValue,
    offsetY: CSSLengthValue,
    blurRadius: CSSLengthValue,
    color: CSSColorValue
) {
    property(
        "drop-shadow",
        "${offsetX.value}${offsetX.unit} ${offsetY.value}${offsetY.unit} ${blurRadius.value}${blurRadius.unit} $color"
    )
}

fun StyleScope.accentColor(value: CSSColorValue) {
    property("accent-color", value)
}

fun StyleScope.backgroundColor(value: String) {
    property("background-color", value)
}

fun StyleScope.overflowWrap(value: OverflowWrap) {
    property("overflow-wrap", value.value)
}

fun StyleScope.pointerEvents(value: PointerEvents) {
    property("pointer-events", value.value)
}

fun StyleScope.textOverflow(value: TextOverflow) {
    property("text-overflow", value.value)
}

fun StyleScope.overflow(value: Overflow) {
    property("overflow", value.value)
}

fun StyleScope.overflowX(value: Overflow) {
    property("overflow-x", value.value)
}

fun StyleScope.overflowY(value: Overflow) {
    property("overflow-y", value.value)
}

fun StyleScope.width(value: LengthKeyword) {
    property("width", value.value)
}

fun StyleScope.height(value: LengthKeyword) {
    property("height", value.value)
}

fun StyleScope.cursor(value: Cursor) {
    property("cursor", value.value)
}

fun StyleScope.borderCollapse(value: BorderCollapse) {
    property("border-collapse", value.value)
}

fun StyleScope.tableLayout(value: TableLayout) {
    property("table-layout", value.value)
}

fun StyleScope.fontWeight(value: FontWeight) {
    property("font-weight", value.value)
}

fun StyleScope.textAlign(value: TextAlign) {
    property("text-align", value.value)
}

fun StyleScope.boxShadow(value: ShadowElevation) {
    boxShadow(
        offsetX = value.offsetX,
        offsetY = value.offsetY,
        blurRadius = value.blurRadius,
        color = value.color
    )
}

fun StyleScope.backgroundClip(value: BackgroundClip) {
    property("background-clip", value.value)
}

fun StyleScope.borderTopLeftRadius(value: CSSNumeric) {
    property("border-top-left-radius", value)
}

fun StyleScope.borderTopRightRadius(value: CSSNumeric) {
    property("border-top-right-radius", value)
}

fun StyleScope.borderBottomLeftRadius(value: CSSNumeric) {
    property("border-bottom-left-radius", value)
}

fun StyleScope.borderBottomRightRadius(value: CSSNumeric) {
    property("border-bottom-right-radius", value)
}

sealed class BackgroundClip(val value: String) {
    object BorderBox : BackgroundClip(value = "border-box")
    object PaddingBox : BackgroundClip(value = "padding-box")
    object ContentBox : BackgroundClip(value = "content-box")
}

sealed class TableLayout(val value: String) {
    object Auto : TableLayout(value = "auto")
    object Fixed : TableLayout(value = "fixed")
}

sealed class BorderCollapse(val value: String) {
    object Collapse : BorderCollapse(value = "collapse")
    object Separate : BorderCollapse(value = "separate")
}

sealed class OverflowWrap(val value: String) {
    object Normal : OverflowWrap(value = "normal")
    object Anywhere : OverflowWrap(value = "anywhere")
    object BreakWord : OverflowWrap(value = "break-word")
}

sealed class PointerEvents(val value: String) {
    object Auto : PointerEvents(value = "auto")
    object None : PointerEvents(value = "none")
    object Stroke : PointerEvents(value = "stroke")
    object Fill : PointerEvents(value = "fill")
}

sealed class TextOverflow(val value: String) {
    object Clip : TextOverflow(value = "clip")
    object Ellipsis : TextOverflow(value = "clip")
    class Custom(value: String) : TextOverflow(value = value)
}

sealed class TextAlign(val value: String) {
    object Start : TextAlign(value = "start")
    object End : TextAlign(value = "end")
    object Center : TextAlign(value = "center")
    object Justify : TextAlign(value = "justify")
}

sealed class FontWeight(val value: String) {
    object Normal : FontWeight(value = "normal")
    object Bold : FontWeight(value = "bold")
    object Bolder : FontWeight(value = "bolder")
    object Lighter : FontWeight(value = "lighter")
    object Initial : FontWeight(value = "initial")
    object Inherit : FontWeight(value = "inherit")
    object W100 : FontWeight(value = "100")
    object W200 : FontWeight(value = "200")
    object W300 : FontWeight(value = "300")
    object W400 : FontWeight(value = "400")
    object W500 : FontWeight(value = "500")
    object W600 : FontWeight(value = "600")
    object W700 : FontWeight(value = "700")
    object W800 : FontWeight(value = "800")
    object W900 : FontWeight(value = "900")
}

object CssGlobalKeyword {
    const val Initial = "initial"
    const val Inherit = "inherit"
    const val Unset = "unset"
    const val Revert = "revert"
}

sealed class Overflow(val value: String) {
    object Visible : Overflow(value = "visible")
    object Hidden : Overflow(value = "hidden")
    object Clip : Overflow(value = "clip")
    object Scroll : Overflow(value = "scroll")
    object Auto : Overflow(value = "auto")
}

sealed class LengthKeyword(val value: String) {
    object MaxContent : LengthKeyword(value = "max-content")
    object MinContent : LengthKeyword(value = "min-content")
    object Auto : LengthKeyword(value = "auto")
}

sealed class Cursor(val value: String) {
    object Auto : Cursor(value = "auto")
    object Pointer : Cursor(value = "pointer")
    object ZoomOut : Cursor(value = "zoom-out")
    object ZoomIn : Cursor(value = "zoom-in")
    object Help : Cursor(value = "help")
    object Wait : Cursor(value = "wait")
    object Crosshair : Cursor(value = "crosshair")
    object NotAllowed : Cursor(value = "not-allowed")
    object Grab : Cursor(value = "grab")
}