package com.example.amnnews.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.amnnews.NewsApplication
import com.example.amnnews.data.remote.model.New
import com.example.amnnews.data.remote.model.SearchNewsModel
import com.example.amnnews.data.remote.model.WnewsModel
import com.example.amnnews.data.repository.MainRepository
import com.example.amnnews.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

//class NewsViewModelMain(private val mainRepository: MainRepository) : ViewModel() {
@RequiresApi(Build.VERSION_CODES.O)
class NewsViewModelMain(
    private val mainRepository: MainRepository,
    private val application: Application
) : AndroidViewModel(application) {


    var breakingNews: MutableLiveData<Resource<WnewsModel>> = MutableLiveData()

    var searchNews:MutableLiveData<Resource<SearchNewsModel>> = MutableLiveData()




    private var _savedNewsListRoomTemp: MutableLiveData<List<New>> = MutableLiveData()

    val savedNewsListRoom: LiveData<List<New>> get() = _savedNewsListRoomTemp





    var language = "en"

//    private var countryCode: String = "us"
    private var countryCode: String = "in"



    init {
        getTopNews(countryCode, language, getCurrentDateInUTC())
        Log.d("AMinKHANDate",getCurrentDateInUTC())
    }






    private fun getCurrentDateInUTC(): String {
        // Create a SimpleDateFormat instance for the desired output format
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        // Create a Calendar instance set to IST (UTC+5:30)
        val istTimeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val calendar = Calendar.getInstance(istTimeZone)

        // Convert IST to UTC by subtracting 5 hours and 30 minutes
        val utcTimeZone = TimeZone.getTimeZone("UTC")
        calendar.add(Calendar.HOUR_OF_DAY, -5)   // Subtract 5 hours
        calendar.add(Calendar.MINUTE, -30)       // Subtract 30 minutes

        // Set SimpleDateFormat to UTC to ensure correct formatting
        sdf.timeZone = utcTimeZone

        // Format the date
        return sdf.format(calendar.time)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getTopNews(countryCode: String, language: String, findTodayDate: String) =
        viewModelScope.launch {
            safeBreakingNewsCall(countryCode, language, findTodayDate)
        }


     fun searchNewsSuspend(searchQuery: String)
    {
        viewModelScope.launch {
            getSearchNews(searchQuery,language,getCurrentDateInUTC())
        }
    }

    private suspend fun getSearchNews(
        searchQuery: String,
        language: String,
        findTodayDate: String
    ) {
        searchNews.postValue(Resource.Loading())
        if (hasInternetConnection()) {

            mainRepository.searchNews(searchQuery, language, findTodayDate)
                .enqueue(object : retrofit2.Callback<SearchNewsModel> {
                    override fun onResponse(
                        call: Call<SearchNewsModel>,
                        response: retrofit2.Response<SearchNewsModel>
                    ) {
                        searchNews.postValue(handleResSearchNews(response))
                    }

                    override fun onFailure(call: Call<SearchNewsModel>, t: Throwable) {
                        searchNews.postValue(Resource.Error("Network Failure"))
                    }
                })
        } else {
            searchNews.postValue(Resource.Error("No internet connection"))
        }
    }

    private fun handleResSearchNews(response: retrofit2.Response<SearchNewsModel>): Resource<SearchNewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                return Resource.Success(resultResponse)

            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun safeBreakingNewsCall(
        countryCode: String,
        language: String,
        findTodayDate: String
    ) {
        breakingNews.postValue(Resource.Loading())
        if (hasInternetConnection()) {

            mainRepository.getBreakingNews(countryCode, language, findTodayDate)
                .enqueue(object : retrofit2.Callback<WnewsModel> {
                    override fun onResponse(
                        call: Call<WnewsModel>,
                        response: retrofit2.Response<WnewsModel>
                    ) {
                        breakingNews.postValue(handleBreakingNewsResponse(response))
                    }

                    override fun onFailure(call: Call<WnewsModel>, t: Throwable) {
                        breakingNews.postValue(Resource.Error("Network Failure"))
                    }
                })
        } else {
            breakingNews.postValue(Resource.Error("No internet connection"))
        }
    }

    private fun handleBreakingNewsResponse(response: retrofit2.Response<WnewsModel>): Resource<WnewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
//                if (breakingNews == null) {
//                    return Resource.Success(resultResponse)
//                } else {
////                    val oldArticles = newsResponse.top_news
////                    val newArticles = resultResponse.top_news
////                    oldArticles?.addAll(newArticles)
//                    return Resource.Success(resultResponse)
//                }

                return Resource.Success(resultResponse)

            }
        }
        return Resource.Error(response.message())
    }

  suspend fun savedNews(news: New)
  {
      mainRepository.savedArticle(news)
  }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


    suspend fun getSavedNewsListAll()
    {
        _savedNewsListRoomTemp.postValue(mainRepository.getAllArticle())

    }

    suspend fun deleteArticle(article: New)
    {
        mainRepository.deleteArticle(article)
    }


}