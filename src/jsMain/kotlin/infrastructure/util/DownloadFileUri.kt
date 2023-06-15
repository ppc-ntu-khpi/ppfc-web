/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package infrastructure.util

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import tables.domain.model.File

fun downloadFileUri(file: File) {
    val blob = Blob(arrayOf(file.fileBytes), BlobPropertyBag("application/octet-stream"))
    val fileUrl = URL.createObjectURL(blob)

    val a = document.createElement("a") as HTMLAnchorElement
    a.href = fileUrl
    a.download = file.fileName
    document.body?.appendChild(a)
    a.click()

    URL.revokeObjectURL(fileUrl)
}