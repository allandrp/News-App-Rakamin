package com.alland.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alland.myapplication.databinding.LoadingPagingBinding

class LoadingStateAdapter(private val retry: () -> (Unit)) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding =
            LoadingPagingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {

        with(holder.binding) {
            if (loadState is LoadState.Error) {
                ivTryAgain.visibility = View.VISIBLE
            }
            pbLoadingPaging.isVisible = loadState is LoadState.Loading
            ivTryAgain.isVisible = loadState is LoadState.Error || loadState !is LoadState.Loading
        }

    }

    class LoadingStateViewHolder(val binding: LoadingPagingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivTryAgain.setOnClickListener { retry.invoke() }
        }
    }
}