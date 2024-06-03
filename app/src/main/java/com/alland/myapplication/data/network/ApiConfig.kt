package com.alland.myapplication.data.network

import com.alland.myapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val newsBaseUrl =  "https://newsapi.org/v2/"

        fun initNewsService(): NewsApiService{
            val apiKeyInterceptor = Interceptor{ chain ->

                val request = chain.request().newBuilder()
                val url = chain.request().url
                val urlWithParams = url.newBuilder().addQueryParameter("apiKey", BuildConfig.ApiKey).build()

                request.url(urlWithParams)

                chain.proceed(request.build())
            }

            val httpClient = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(apiKeyInterceptor).addInterceptor(httpClient).build()

            val retrofit =
                Retrofit.Builder().baseUrl(newsBaseUrl).addConverterFactory(GsonConverterFactory.create())
                    .client(client).build()

            return retrofit.create(NewsApiService::class.java)
        }
    }
}