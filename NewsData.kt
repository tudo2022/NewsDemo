package com.example.newsdemo

data class NewsData(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>?
)

data class Article(
    val author: String?,
    val title: String?,
    val urlToImage: String?
)