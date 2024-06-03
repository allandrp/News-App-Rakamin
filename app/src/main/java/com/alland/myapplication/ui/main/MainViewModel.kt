package com.alland.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alland.myapplication.data.model.ArticlesItem
import com.alland.myapplication.data.repository.NewsRepository

class MainViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private var _headlineNews: MediatorLiveData<PagingData<ArticlesItem>> = MediatorLiveData()
    val headlineNews: LiveData<PagingData<ArticlesItem>> = _headlineNews

    private var _allNews: MediatorLiveData<PagingData<ArticlesItem>> = MediatorLiveData()
    val allNews: LiveData<PagingData<ArticlesItem>> = _allNews

    fun getAllNews(query: String){
        val result = newsRepository.getAllNews(query = query).cachedIn(viewModelScope)
        _allNews.addSource(result) {
            _allNews.value = it
        }
    }

    fun getHeadlineNews(){
        val result = newsRepository.getHeadlineNews().cachedIn(viewModelScope)
        _headlineNews.addSource(result) {
            _headlineNews.value = it
        }
    }

}