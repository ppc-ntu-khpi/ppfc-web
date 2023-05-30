/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.Course
import tables.domain.model.Group
import tables.domain.repository.GroupsRepository

class ObservePagedGroups(
    private val groupsRepository: GroupsRepository
) : PagingInteractor<ObservePagedGroups.Params, Group>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Group>> {
        return Pager(config = params.pagingConfig) {
            groupsRepository.getGroupsPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery,
                course = params.course
            )
        }.flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val searchQuery: String? = null,
        val course: Course? = null
    ) : PagingInteractor.Params<Group>
}