/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.Discipline
import tables.domain.repository.DisciplinesRepository

class ObservePagedDisciplines(
    private val disciplinesRepository: DisciplinesRepository
) : PagingInteractor<ObservePagedDisciplines.Params, Discipline>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Discipline>> {
        return Pager(config = params.pagingConfig) {
            disciplinesRepository.getDisciplinesPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery
            )
        }.flow
    }

    data class Params(
        val searchQuery: String,
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<Discipline>
}