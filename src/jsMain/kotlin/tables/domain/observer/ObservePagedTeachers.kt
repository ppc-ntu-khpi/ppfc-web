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
import tables.domain.model.Teacher
import tables.domain.repository.TeachersRepository

class ObservePagedTeachers(
    private val teachersRepository: TeachersRepository
) : PagingInteractor<ObservePagedTeachers.Params, Teacher>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Teacher>> {
        return Pager(config = params.pagingConfig) {
            teachersRepository.getTeachersPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery,
                discipline = params.discipline
            )
        }.flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val searchQuery: String? = null,
        val discipline: Discipline? = null
    ) : PagingInteractor.Params<Teacher>
}