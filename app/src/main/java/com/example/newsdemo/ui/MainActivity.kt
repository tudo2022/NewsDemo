package com.example.newsdemo.ui

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemo.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "hm"
const val ApiKey = "282282d0e89141e1ba55a7b0c3ed5ff5"

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vm: MainViewModel by viewModels()

        recyclerView = findViewById(R.id.rv_news)
        progressBar = findViewById(R.id.pb_news)
        errorTextView = findViewById(R.id.tv_error)

        recyclerView.adapter = NewsAdapter()
        vm.getData()

        lifecycleScope.launch {
            vm.listFlow.collectLatest {
                (recyclerView.adapter as NewsAdapter).setData(it)
            }
        }
    }
}