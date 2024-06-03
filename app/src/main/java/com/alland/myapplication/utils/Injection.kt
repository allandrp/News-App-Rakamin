package com.alland.myapplication.utils

import com.alland.myapplication.data.network.ApiConfig
import com.alland.myapplication.data.repository.NewsRepository

object Injection {

    fun provideNewsRepository(): NewsRepository{
        val apiService = ApiConfig.initNewsService()
        return NewsRepository(apiService)
    }
}