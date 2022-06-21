package com.example.newsdemo.ui

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemo.BR
import com.example.newsdemo.R
import com.example.newsdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "hm"
const val ApiKey = "282282d0e89141e1ba55a7b0c3ed5ff5"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: MainViewModel by viewModels()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.rvNews.adapter = MainAdapter()

        lifecycleScope.launch {
            vm.listFlow.collectLatest {
                (binding.rvNews.adapter as MainAdapter).submitList(it)
            }
        }
        vm.getData()
    }
}