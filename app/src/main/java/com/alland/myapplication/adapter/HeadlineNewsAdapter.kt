package com.alland.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alland.myapplication.R
import com.alland.myapplication.data.model.ArticlesItem
import com.alland.myapplication.databinding.ItemNewsHeadlinesBinding
import com.alland.myapplication.utils.util
import com.bumptech.glide.Glide

class HeadlineNewsAdapter(
    val onClick: (ArticlesItem) -> Unit
) :
    PagingDataAdapter<ArticlesItem, HeadlineNewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {
    class NewsViewHolder(val binding: ItemNewsHeadlinesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        val author = item?.author ?: "Unknown"
        val title = item?.title ?: "Unknown"
        val date = item?.publishedAt ?: "Unknown"

        with(holder.binding) {
            Glide.with(holder.itemView.context).load(item?.urlToImage)
                .placeholder(R.drawable.no_image_placeholder).into(ivHeadlineNews)
            tvHeadlineAuthor.text = "By ${author}"
            tvHeadlineTitle.text = title
            tvHeadlineDate.text = util.convertDateStringToFormattedString(date)
        }
        holder.binding.ivHeadlineNews.setOnClickListener {
            if (item != null) {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            ItemNewsHeadlinesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(view)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.content == newItem.content
            }
        }
    }
}