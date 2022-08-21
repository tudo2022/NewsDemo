package com.example.newsdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemo.BR
import com.example.newsdemo.R
import com.example.newsdemo.data.ArticleModel
import com.example.newsdemo.databinding.ItemNewsBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private val list: MutableList<ArticleModel> = emptyList<ArticleModel>().toMutableList()

    class ViewPagerViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding: ViewDataBinding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.binding.setVariable(BR.articleModel,list[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = list.size

    fun setData(list: List<ArticleModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}