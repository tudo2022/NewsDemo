package com.example.newsdemo

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val data: NewsData?) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
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
        val articles = data?.articles?.get(position)
        holder.author.text = articles?.author
        holder.title.text = articles?.title
        if (articles?.urlToImage != null) {
            val imageUrl = Uri.parse(articles.urlToImage)
            Glide.with(holder.imageView).load(imageUrl).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = data?.articles?.size!!
}