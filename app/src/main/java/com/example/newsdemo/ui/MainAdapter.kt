package com.example.newsdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemo.BR
import com.example.newsdemo.data.ArticleModel
import com.example.newsdemo.databinding.ItemNewsBinding


class MainAdapter : ListAdapter<ArticleModel, MainAdapter.NewsBindingViewHolder>(
    object : DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }
    }) {
    class NewsBindingViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsBindingViewHolder {
        val binding: ViewDataBinding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsBindingViewHolder, position: Int) {
        holder.binding.setVariable(BR.articleModel, currentList[position])
        holder.binding.executePendingBindings()
    }
}