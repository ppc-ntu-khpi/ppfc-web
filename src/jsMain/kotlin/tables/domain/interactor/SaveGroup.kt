/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.interactor

import core.domain.Interactor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tables.domain.model.Group
import tables.domain.repository.GroupsRepository

class SaveGroup(
    private val groupsRepository: GroupsRepository
) : Interactor<SaveGroup.Params, Unit>() {

    override suspend fun doWork(params: Params): Unit = withContext(Dispatchers.Default) {
        groupsRepository.saveGroup(group = params.group)
    }

    data class Params(val group: Group)
}