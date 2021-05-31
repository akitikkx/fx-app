package com.ahmedtikiwa.fxapp.ui.graph

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedtikiwa.fxapp.databinding.LoadStateFooterBinding

class HistoryLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<HistoryLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val withDataBinding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonLoadStateRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBarLoadState.isVisible = loadState is LoadState.Loading
                buttonLoadStateRetry.isVisible = loadState is LoadState.Error
                textViewLoadStateError.isVisible = loadState is LoadState.Error

                if (loadState is LoadState.Error) {
                    textViewLoadStateError.text = loadState.error.localizedMessage
                }
            }
        }
    }
}