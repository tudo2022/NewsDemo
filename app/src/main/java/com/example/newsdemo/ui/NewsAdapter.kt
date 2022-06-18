package com.example.newsdemo.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsdemo.R
import com.example.newsdemo.data.ArticleModel

class NewsAdapter() :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private val list: MutableList<ArticleModel> = emptyList<ArticleModel>().toMutableList()

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val author: TextView = view.findViewById(R.id.tv_author)
        val imageView: ImageView = view.findViewById(R.id.iv_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val articles = list[position]
        holder.author.text = articles.author
        holder.title.text = articles.title
        val imageUrl = Uri.parse(articles.urlToImage)
        Glide.with(holder.imageView).load(imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<ArticleModel>) {
        this.list.clear()
        this.list.addAll(list)
    }
}