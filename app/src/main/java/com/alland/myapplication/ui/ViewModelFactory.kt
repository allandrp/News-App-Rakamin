package com.alland.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alland.myapplication.data.repository.NewsRepository
import com.alland.myapplication.ui.main.MainViewModel
import com.alland.myapplication.utils.Injection

class ViewModelFactory(private val newsRepository: NewsRepository):
    ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return when{

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(newsRepository) as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel Class: "+ modelClass.name)
            }
        }
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(): ViewModelFactory{
            if(instance == null){
                synchronized(this){
                    instance = ViewModelFactory(Injection.provideNewsRepository())
                }
            }

            return instance as ViewModelFactory
        }
    }
}