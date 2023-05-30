/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.GroupsDao
import tables.data.mapper.toDomain
import tables.data.mapper.toRequest
import tables.domain.model.Course
import tables.domain.model.Group
import tables.domain.model.Id
import tables.domain.repository.GroupsRepository

class GroupsRepositoryImpl(
    private val groupsDao: GroupsDao
) : GroupsRepository {

    private var groupsPagingSource: PagingSource<Long, Group>? = null

    override suspend fun saveGroup(group: Group) {
        if (group.id is Id.Value) {
            groupsDao.updateGroup(groupRequest = group.toRequest(), id = group.id.value)
        } else {
            groupsDao.saveGroup(groupRequest = group.toRequest())
        }
        groupsPagingSource?.invalidate()
    }

    override suspend fun deleteGroups(ids: Set<Id>) {
        groupsDao.deleteGroups(ids = ids.map { it.value }.toSet())
        groupsPagingSource?.invalidate()
    }

    override fun getGroupsPagingSource(
        pageSize: Long,
        searchQuery: String?,
        course: Course?
    ) = object : PagingSource<Long, Group>() {

        init {
            groupsPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, Group>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Group> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                groupsDao.getGroups(
                    limit = pageSize,
                    offset = offset,
                    searchQuery = searchQuery,
                    courseId = course?.id?.value
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