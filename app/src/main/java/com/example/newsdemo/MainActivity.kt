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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView=findViewById<RecyclerView>(R.id.rv_news)
        val progressBar=findViewById<ProgressBar>(R.id.pb_news)
        val errorTextView=findViewById<TextView>(R.id.tv_error)
        recyclerView.layoutManager=LinearLayoutManager(this)
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/")
            .build()
            .create(NewsApi::class.java)
        val news = retrofit.getNews("tesla", "publishAt", "282282d0e89141e1ba55a7b0c3ed5ff5")
        news.enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                progressBar.visibility=View.VISIBLE
                val newsData=response.body()
                if (response.isSuccessful){
                    recyclerView.adapter= newsData?.let { NewsAdapter(it) }
                    recyclerView.visibility= View.VISIBLE
                    progressBar.visibility=View.INVISIBLE
                }else{
                    recyclerView.visibility=View.INVISIBLE
                    progressBar.visibility=View.INVISIBLE
                    errorTextView.visibility=View.VISIBLE
                }
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                recyclerView.visibility=View.INVISIBLE
                progressBar.visibility=View.INVISIBLE
                errorTextView.visibility=View.VISIBLE
                Log.e("hm", "err",t)
            }
        })

        //下拉刷新
        val swipeRefreshLayout=findViewById<SwipeRefreshLayout>(R.id.srl_news)
        swipeRefreshLayout.setOnRefreshListener {
            val news2 = retrofit.getNews("apple", "popularity", "282282d0e89141e1ba55a7b0c3ed5ff5")
            news2.enqueue(object : Callback<NewsData> {
                override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                    progressBar.visibility=View.VISIBLE
                    val newsData=response.body()
                    if (response.isSuccessful){
                        recyclerView.adapter= newsData?.let { NewsAdapter(it) }
                        recyclerView.visibility= View.VISIBLE
                        progressBar.visibility=View.INVISIBLE
                    }else{
                        recyclerView.visibility=View.INVISIBLE
                        progressBar.visibility=View.INVISIBLE
                        errorTextView.visibility=View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<NewsData>, t: Throwable) {
                    recyclerView.visibility=View.INVISIBLE
                    progressBar.visibility=View.INVISIBLE
                    errorTextView.visibility=View.VISIBLE
                    Log.e("hm", "err",t)
                }
            })
            swipeRefreshLayout.isRefreshing=false
        }

        //上拉加载更多
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)){
                    val news3 = retrofit.getNews("tesla", "publishAt", "282282d0e89141e1ba55a7b0c3ed5ff5")
                    news3.enqueue(object : Callback<NewsData> {
                        override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                            progressBar.visibility=View.VISIBLE
                            val newsData=response.body()
                            if (response.isSuccessful){
                                recyclerView.adapter= newsData?.let { NewsAdapter(it) }
                                recyclerView.visibility= View.VISIBLE
                                progressBar.visibility=View.INVISIBLE
                            }else{
                                recyclerView.visibility=View.INVISIBLE
                                progressBar.visibility=View.INVISIBLE
                                errorTextView.visibility=View.VISIBLE
                            }
                        }

                        override fun onFailure(call: Call<NewsData>, t: Throwable) {
                            recyclerView.visibility=View.INVISIBLE
                            progressBar.visibility=View.INVISIBLE
                            errorTextView.visibility=View.VISIBLE
                            Log.e("hm", "err",t)
                        }
                    })
                }
            }
        })
    }
}