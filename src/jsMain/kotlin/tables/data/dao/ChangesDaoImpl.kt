/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import api.ApiClient
import core.domain.UnexpectedErrorException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import tables.data.model.ChangeRequest
import tables.data.model.ChangeResponse
import tables.data.model.GenerateChangesDocumentResponse
import tables.domain.interactor.NoChangesException
import tables.domain.model.File

class ChangesDaoImpl(
    private val apiClient: ApiClient
) : ChangesDao {

    override suspend fun saveChange(changeRequest: ChangeRequest) {
        apiClient.client.post(PATH) {
            contentType(ContentType.Application.Json)
            setBody(changeRequest)
        }
    }

    override suspend fun saveChanges(changesRequests: List<ChangeRequest>) {
        apiClient.client.post("$PATH/multiple") {
            contentType(ContentType.Application.Json)
            setBody(changesRequests)
        }
    }

    override suspend fun updateChange(changeRequest: ChangeRequest, id: Long) {
        apiClient.client.put("$PATH/$id") {
            contentType(ContentType.Application.Json)
            setBody(changeRequest)
        }
    }

    override suspend fun updateChanges(changesRequests: Map<Long, ChangeRequest>) {
        apiClient.client.put("$PATH/multiple") {
            contentType(ContentType.Application.Json)
            setBody(changesRequests)
        }
    }

    override suspend fun deleteChanges(ids: Set<Long>) {
        apiClient.client.delete("$PATH/${ids.joinToString(",")}")
    }

    override suspend fun deleteAllChanges() {
        apiClient.client.delete("$PATH/all")
    }

    override suspend fun getChanges(
        limit: Long,
        offset: Long,
        date: String?,
        isNumerator: Boolean?,
        groupId: Long?,
        teacherId: Long?
    ): List<ChangeResponse> {
        return apiClient.client.get(PATH) {
            if (limit > 0) parameter("limit", limit)
            if (offset > 0) parameter("offset", offset)
            if (date != null) parameter("date", date)
            if (isNumerator != null) parameter("isNumerator", isNumerator)
            if (groupId != null) parameter("groupId", groupId)
            if (teacherId != null) parameter("teacherId", teacherId)
        }.body()
    }

    override suspend fun exportChangesToDocument(date: String): File {
        val response: GenerateChangesDocumentResponse = apiClient.client.get("$PATH/generateWordDocument") {
            parameter("date", date)
        }.body()

        if(response.error != null) {
            if(response.error == "NO_CHANGES") throw NoChangesException()
            throw UnexpectedErrorException()
        }

        if(response.fileName == null || response.fileBytes == null) throw UnexpectedErrorException()

        return File(
            fileName = response.fileName,
            fileBytes = response.fileBytes
        )
    }

    private companion object {
        const val PATH = "change"
    }
}