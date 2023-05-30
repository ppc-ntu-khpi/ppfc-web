/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.Classroom
import tables.domain.repository.ClassroomsRepository

class ObservePagedClassrooms(
    private val classroomsRepository: ClassroomsRepository
) : PagingInteractor<ObservePagedClassrooms.Params, Classroom>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Classroom>> {
        return Pager(config = params.pagingConfig) {
            classroomsRepository.getClassroomsPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery
            )
        }.flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val searchQuery: String? = null
    ) : PagingInteractor.Params<Classroom>
}