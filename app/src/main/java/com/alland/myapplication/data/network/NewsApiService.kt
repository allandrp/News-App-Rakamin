package com.alland.myapplication.data.network

import com.alland.myapplication.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getHeadLineNews(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): NewsResponse

    @GET("everything")
    suspend fun getAllNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("language") language: String
    ): NewsResponse
}