/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.Subject
import tables.domain.repository.SubjectsRepository

class ObservePagedSubjects(
    private val subjectsRepository: SubjectsRepository
) : PagingInteractor<ObservePagedSubjects.Params, Subject>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Subject>> {
        return Pager(config = params.pagingConfig) {
            subjectsRepository.getSubjectsPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery
            )
        }.flow
    }

    data class Params(
        val searchQuery: String,
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<Subject>
}