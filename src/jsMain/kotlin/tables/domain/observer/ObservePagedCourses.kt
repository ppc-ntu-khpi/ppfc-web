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
import tables.domain.repository.CoursesRepository

class ObservePagedCourses(
    private val coursesRepository: CoursesRepository
) : PagingInteractor<ObservePagedCourses.Params, Course>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Course>> {
        return Pager(config = params.pagingConfig) {
            coursesRepository.getCoursesPagingSource(
                pageSize = params.pagingConfig.pageSize.toLong(),
                searchQuery = params.searchQuery
            )
        }.flow
    }

    data class Params(
        val searchQuery: String,
        override val pagingConfig: PagingConfig
    ) : PagingInteractor.Params<Course>
}