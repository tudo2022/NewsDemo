package com.example.newsdemo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "hm"
const val ApiKey = "282282d0e89141e1ba55a7b0c3ed5ff5"

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
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
        val news = retrofit.create(NewsApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Dispatchers.IO", Thread.currentThread().name) // DefaultDispatcher-worker-1 线程
            if (isNetworkAvailable(applicationContext)) {
                val response = news.getNews("tesla", "publishAt", ApiKey)
                withContext(Dispatchers.Main) {
                    Log.d("Dispatchers.Main", Thread.currentThread().name) // 在主线程
                    onResponse(response)
                }
            } else {
                Log.e(TAG, "err")
                recyclerView.visibility = View.INVISIBLE
                progressBar.visibility = View.INVISIBLE
                errorTextView.visibility = View.VISIBLE
            }
        }

        // 下拉刷新
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.srl_news)
        swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                val response2 = news.getNews("apple", "popularity", ApiKey)
                withContext(Dispatchers.Main) {
                    onResponse(response2)
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        // 上拉加载更多
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response3 =
                            news.getNews("tesla", "publishAt", ApiKey)
                        withContext(Dispatchers.Main) {
                            onResponse(response3)
                        }
                    }
                }
            }
        })
    }


    private fun onResponse(response: Response<NewsData>) {
        if (response.isSuccessful) {
            val list: MutableList<ArticleModel> = ArrayList()
            val newsData = response.body()
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

    // 在进行网络请求之前检查网络是否连接
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}