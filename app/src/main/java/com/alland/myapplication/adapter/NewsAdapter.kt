package com.alland.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alland.myapplication.R
import com.alland.myapplication.data.model.ArticlesItem
import com.alland.myapplication.databinding.ItemNewsBinding
import com.alland.myapplication.utils.util
import com.bumptech.glide.Glide

class NewsAdapter(
    val onClick: (ArticlesItem) -> Unit
) : PagingDataAdapter<ArticlesItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {
    class NewsViewHolder(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        val author = item?.author ?: "Unknown"
        val title = item?.title ?: "Unknown"
        val date = item?.publishedAt ?: "Unknown"
        val description = item?.description ?: "Unknown"

        with(holder.binding) {
            Glide.with(holder.itemView.context).load(item?.urlToImage)
                .placeholder(R.drawable.no_image_placeholder).into(ivNews)
            tvNewsAuthor.text = "By ${author}"
            tvNewsTitle.text = title
            tvNewsDate.text = util.convertDateStringToFormattedString(date) ?: "-"
            tvNewsDescription.text = description
        }
        holder.itemView.setOnClickListener {
            if (item != null) {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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