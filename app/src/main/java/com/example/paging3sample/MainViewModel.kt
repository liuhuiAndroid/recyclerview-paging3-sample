package com.example.paging3sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3sample.api.GitHubService
import com.example.paging3sample.model.Repo
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {

    private val pageSize = 5

    private val gitHubService = GitHubService.create()

    fun getPagingData(): Flow<PagingData<Repo>> {
        // cachedIn() 运算符使数据流可共享，并使用提供的 CoroutineScope 缓存加载的数据。
        return Pager(PagingConfig(pageSize)){
            RepoPagingSource(gitHubService)
        }.flow.cachedIn(viewModelScope)
    }

}