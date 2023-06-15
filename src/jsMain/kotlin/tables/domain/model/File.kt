/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.model

data class File(
    val fileName: String,
    val fileBytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as File

        if (fileName != other.fileName) return false
        return fileBytes.contentEquals(other.fileBytes)
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + fileBytes.contentHashCode()
        return result
    }
}
