/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.ScheduleDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.*
import tables.domain.repository.ScheduleRepository

class ScheduleRepositoryImpl(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {

    private var schedulePagingSource: PagingSource<Long, ScheduleItem>? = null

    override suspend fun saveScheduleItem(scheduleItem: ScheduleItem) {
        if (scheduleItem.id is Id.Value) {
            scheduleDao.updateSchedule(scheduleRequest = scheduleItem.toRequest(), id = scheduleItem.id.value)
        } else {
            scheduleDao.saveScheduleItem(scheduleRequest = scheduleItem.toRequest())
        }
        schedulePagingSource?.invalidate()
    }

    override suspend fun deleteScheduleItems(ids: Set<Id>) {
        scheduleDao.deleteScheduleItems(ids = ids.map { it.value }.toSet())
        schedulePagingSource?.invalidate()
    }

    override suspend fun deleteAllScheduleItems() {
        scheduleDao.deleteAllScheduleItems()
        schedulePagingSource?.invalidate()
    }

    override fun getSchedulePagingSource(
        pageSize: Long,
        dayNumber: DayNumber?,
        weekAlternation: WeekAlternation?,
        group: Group?,
        teacher: Teacher?
    ) = object : PagingSource<Long, ScheduleItem>() {

        init {
            schedulePagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, ScheduleItem>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ScheduleItem> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                scheduleDao.getScheduleItems(
                    limit = pageSize,
                    offset = offset,
                    dayNumber = dayNumber?.number,
                    isNumerator = weekAlternation?.isNumerator,
                    groupId = group?.id?.value,
                    teacherId = teacher?.id?.value
                )
            } catch (e: ApiException) {
                return LoadResult.Error(e)
            }

            return LoadResult.Page(
                data = result.map { it.toDomain() },
                prevKey = if (page <= 0) null else page - 1,
                nextKey = if (result.size < pageSize) null else page + 1,
                itemsBefore = offset.toInt()
            )
        }
    }
}