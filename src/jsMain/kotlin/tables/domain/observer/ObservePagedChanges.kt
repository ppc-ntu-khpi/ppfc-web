/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.Change
import tables.domain.model.Group
import tables.domain.model.Teacher
import tables.domain.model.WeekAlternation
import tables.domain.repository.ChangesRepository
import kotlin.js.Date

class ObservePagedChanges(
    private val changesRepository: ChangesRepository
) : PagingInteractor<ObservePagedChanges.Params, Change>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Change>> {
        return Pager(config = params.pagingConfig) {
            changesRepository.getChangesPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                date = params.date,
                weekAlternation = params.weekAlternation,
                group = params.group,
                teacher = params.teacher
            )
        }.flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val date: Date? = null,
        val weekAlternation: WeekAlternation? = null,
        val group: Group? = null,
        val teacher: Teacher? = null
    ) : PagingInteractor.Params<Change>
}