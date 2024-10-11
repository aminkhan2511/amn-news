package com.example.amnnews.data.repository

import com.example.amnnews.data.local.DatabaseRoom
import com.example.amnnews.data.remote.RetrofitInstance
import com.example.amnnews.data.remote.model.New

class MainRepository(private  val articleDataBase: DatabaseRoom) {





    suspend fun getBreakingNews(countryCode: String, language:String,date:String) =
        RetrofitInstance.api.getTopNews(countryCode, language,date)//returning response of api (data) to viewmodel

    suspend fun searchNews(query:String,language: String,date: String)=RetrofitInstance.api.searchNews(query,language,date)


    suspend fun savedArticle(article: New):Unit=articleDataBase.getDaoInterfaceInstance().insert(article)


    suspend fun getAllArticle():List<New> = articleDataBase.getDaoInterfaceInstance().getAll()


    suspend fun deleteArticle(article: New)=articleDataBase.getDaoInterfaceInstance().delete(article)


}