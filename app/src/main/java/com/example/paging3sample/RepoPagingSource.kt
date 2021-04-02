package com.example.paging3sample

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3sample.api.GitHubService
import com.example.paging3sample.model.Repo
import java.lang.Exception

/**
 * PagingSource 用于标识数据源
 */
class RepoPagingSource(private val backend: GitHubService) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize
            val response = backend.searchRepos(pageNumber, pageSize)
            val prevKey = if (pageNumber > 1) pageNumber - 1 else null
            val nextKey = if (response.items.isNotEmpty()) pageNumber + 1 else null
            LoadResult.Page(response.items, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? = null

}