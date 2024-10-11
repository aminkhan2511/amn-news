package com.example.amnnews.data.remote.model

data class SearchNewsModel(
    val available: Int,
    val news: List<New>,
    val number: Int,
    val offset: Int
)