/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package coreui.theme

import org.jetbrains.compose.web.css.CSSColorValue

sealed class AppSvgIcon(val value: String) {
    class Calendar(private val color: CSSColorValue) : AppIconClass(
        value = """
        url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" viewBox="0 0 24 24"><path fill="$color" d="M20 3h-1V1h-2v2H7V1H5v2H4c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 18H4V8h16v13z"/></svg>'
    """.trimIndent()
    )
}