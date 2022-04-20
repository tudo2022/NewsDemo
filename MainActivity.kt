package com.example.newsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "hm"
const val ApiKey = "282282d0e89141e1ba55a7b0c3ed5ff5"

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var errorTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_news)
        progressBar = findViewById(R.id.pb_news)
        errorTextView = findViewById(R.id.tv_error)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/")
            .build()
            .create(NewsApi::class.java)
        val news = retrofit.getNews("tesla", "publishAt", ApiKey)
        enqueue(news)

        // 下拉刷新
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.srl_news)
        swipeRefreshLayout.setOnRefreshListener {
            val news2 = retrofit.getNews("tesla", "publishAt", ApiKey)
            enqueue(news2)
            swipeRefreshLayout.isRefreshing = false
        }

        // 上拉加载更多
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    val news3 =
                        retrofit.getNews("tesla", "publishAt", ApiKey)
                    enqueue(news3)
                }
            }
        })
    }

    private fun enqueue(news: Call<NewsData>) {
        news.enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                progressBar.visibility = View.VISIBLE
                val newsData = response.body()
                if (response.isSuccessful) {
                    val list: MutableList<ArticleModel> = ArrayList()
                    newsData?.articles?.forEach {
                        list.add(
                            ArticleModel(
                                author = it.author.orEmpty(),
                                title = it.title.orEmpty(),
                                urlToImage = it.urlToImage.orEmpty()
                            )
                        )
                    }
                    recyclerView.adapter = NewsAdapter(list)
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.visibility = View.INVISIBLE
                } else {
                    recyclerView.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                    errorTextView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                recyclerView.visibility = View.INVISIBLE
                progressBar.visibility = View.INVISIBLE
                errorTextView.visibility = View.VISIBLE
                Log.e(TAG, "err", t)
            }
        })
    }

}
