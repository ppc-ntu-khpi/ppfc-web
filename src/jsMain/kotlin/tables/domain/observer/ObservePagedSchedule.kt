/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.domain.observer

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import core.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import tables.domain.model.*
import tables.domain.repository.ScheduleRepository

class ObservePagedSchedule(
    private val scheduleRepository: ScheduleRepository
) : PagingInteractor<ObservePagedSchedule.Params, ScheduleItem>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<ScheduleItem>> {
        return Pager(config = params.pagingConfig) {
            scheduleRepository.getSchedulePagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                dayNumber = params.dayNumber,
                weekAlternation = params.weekAlternation,
                group = params.group,
                teacher = params.teacher
            )
        }.flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val dayNumber: DayNumber? = null,
        val weekAlternation: WeekAlternation? = null,
        val group: Group? = null,
        val teacher: Teacher? = null
    ) : PagingInteractor.Params<ScheduleItem>
}