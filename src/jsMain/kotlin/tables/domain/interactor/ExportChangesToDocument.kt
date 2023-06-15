/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.File
import tables.domain.repository.ChangesRepository
import kotlin.js.Date

class ExportChangesToDocument(
    private val changesRepository: ChangesRepository
) : Interactor<ExportChangesToDocument.Params, File>() {

    override suspend fun doWork(params: Params): File = withContext(Dispatchers.Default) {
        return@withContext changesRepository.exportChangesToDocument(date = params.date)
    }

    data class Params(val date: Date)
}