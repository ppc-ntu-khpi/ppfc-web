/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.repository

import androidx.paging.PagingState
import app.cash.paging.PagingSource
import core.domain.ApiException
import tables.data.dao.UsersDao
import tables.data.mapper.toDomain
import tables.domain.model.Id
import tables.domain.model.User
import tables.domain.repository.UsersRepository

class UsersRepositoryImpl(
    private val usersDao: UsersDao
) : UsersRepository {

    private var usersPagingSource: PagingSource<Long, User>? = null

    override suspend fun deleteUsers(ids: Set<Id>) {
        usersDao.deleteUsers(ids = ids.map { it.value }.toSet())
        usersPagingSource?.invalidate()
    }

    override fun getUsersPagingSource(
        pageSize: Long,
        searchQuery: String?
    ) = object : PagingSource<Long, User>() {

        init {
            usersPagingSource = this
        }

        override fun getRefreshKey(state: PagingState<Long, User>): Long? {
            return state.anchorPosition?.let { anchorPosition ->
                anchorPosition / pageSize
            }
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, User> {
            val page = params.key ?: 0L
            val offset = page * pageSize

            val result = try {
                usersDao.getUsers(
                    limit = pageSize,
                    offset = offset,
                    searchQuery = searchQuery
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