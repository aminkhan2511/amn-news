package com.example.amnnews.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amnnews.data.repository.MainRepository

class NewsViewModelProviderFactory(val mainRepository: MainRepository, val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModelMain(mainRepository,application) as T
    }
}