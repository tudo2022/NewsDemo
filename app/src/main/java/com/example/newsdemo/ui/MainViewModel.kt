package com.example.newsdemo.ui

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.newsdemo.api.NewsApi
import com.example.newsdemo.data.ArticleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    val listFlow = MutableStateFlow<List<ArticleModel>>(emptyList())
    private var modelList:ArrayList<ArticleModel> = ArrayList()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://newsapi.org/")
        .build()

    fun getData() {
        viewModelScope.launch {
            try {
                val response = retrofit
                    .create(NewsApi::class.java)
                    .getNews("tesla", "publishAt", ApiKey)
                Log.d(TAG,response.toString())
                response.articles?.forEach {
                    modelList.add(
                        ArticleModel(
                            urlToImage = it.urlToImage.orEmpty(),
                            title = it.title.orEmpty(),
                            author = it.author.orEmpty()
                        )
                    )
                }
                listFlow.emit(modelList)
            } catch (e: Throwable) {
                Log.e(TAG, "err")
            }
        }
    }
}
