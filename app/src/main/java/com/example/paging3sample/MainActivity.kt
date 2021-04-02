package com.example.paging3sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val repoAdapter = RepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = repoAdapter.withLoadStateFooter(FooterAdapter { repoAdapter::retry })
        
        lifecycleScope.launch {
            viewModel.getPagingData().collect { pagingData ->
                repoAdapter.submitData(pagingData)
            }
            repoAdapter.loadStateFlow.collect {

            }
        }
        repoAdapter.addLoadStateListener {
            when (it.refresh) {
                // 没有正在执行的加载操作且没有错误
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
                // 有正在执行的加载操作
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                }
                // 出现错误
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "Load Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}