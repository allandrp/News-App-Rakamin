package com.alland.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.alland.myapplication.data.model.ArticlesItem
import com.alland.myapplication.data.network.NewsApiService
import com.alland.myapplication.data.paging.HeadlineNewsPagingSource
import com.alland.myapplication.data.paging.NewsPagingSource

class NewsRepository(private val apiService: NewsApiService) {

    fun getHeadlineNews(
        country: String = "us",
        pageSize: Int = 10
    ): LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize
            ),
            pagingSourceFactory = {
                HeadlineNewsPagingSource(apiService, country)
            }
        ).liveData
    }

    fun getAllNews(
        query: String = "olahraga AND politik AND teknologi AND pendidikan",
        pageSize: Int = 10
    ): LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize
            ),
            pagingSourceFactory = {
                NewsPagingSource(apiService, query)
            }
        ).liveData
    }
}