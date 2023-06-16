/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Change

class SaveChanges(
    private val saveChange: SaveChange
) : Interactor<SaveChanges.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        params.changes.forEach { change ->
            saveChange.executeSync(
                params = SaveChange.Params(change = change)
            )
        }
    }

    data class Params(val changes: List<Change>)
}