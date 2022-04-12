package com.example.newsdemo

data class NewsData(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Articles>?
)

data class Articles(
    val author: String?,
    val title: String?,
    val urlToImage: String?
)
