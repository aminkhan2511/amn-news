package com.example.amnnews.data.remote.model

data class WnewsModel(
    val country: String,
    val language: String,
    val top_news: List<TopNew>
)