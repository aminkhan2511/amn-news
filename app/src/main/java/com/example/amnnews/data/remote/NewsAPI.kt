package com.example.amnnews.data.remote



import com.example.amnnews.data.remote.model.SearchNewsModel
import com.example.amnnews.data.remote.model.WnewsModel
import com.example.amnnews.util.Constant.Companion.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsAPI {

//    @GET("v2/top-headlines")
//    suspend fun getBreakingNews(
//        @Query("country")
//        countryCode: String = "in",
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey")
//        apiKey: String = API_KEY
//    ): Response<NewsResponse>
//
//    @GET("v2/everything")
//    suspend fun searchForNews(
//        @Query("q")
//        searchQuery: String,
//        @Query("page")
//        pageNumber: Int = 1,
//        @Query("apiKey")
//        apiKey: String = API_KEY
//    ): Response<NewsResponse>


    @GET("top-news")
     fun getTopNews(
        @Query("source-country") sourceCountry: String,
        @Query("language") language: String,
        @Query("date") date: String,
        @Header("x-api-key") apiKey: String=API_KEY
    ): Call<WnewsModel>

    @GET("search-news")
      fun searchNews(
        @Query("text") text: String,
        @Query("language") language: String,
        @Query("earliest-publish-date") date: String,
        @Header("x-api-key") apiKey: String=API_KEY
    ): Call<SearchNewsModel>
}