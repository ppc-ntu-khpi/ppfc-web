/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.User
import tables.domain.repository.UsersRepository

class ObservePagedUsers(
    private val usersRepository: UsersRepository
) : PagingInteractor<ObservePagedUsers.Params, User>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<User>> {
        return Pager(config = params.pagingConfig) {
            usersRepository.getUsersPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery
            )
        }.flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val searchQuery: String? = null
    ) : PagingInteractor.Params<User>
}