package com.ahmedtikiwa.fxapp.ui.graph

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedtikiwa.fxapp.R
import com.ahmedtikiwa.fxapp.database.DatabaseHistory
import com.ahmedtikiwa.fxapp.databinding.LayoutHistoryPagingItemBinding

class HistoryPagingAdapter : PagingDataAdapter<DatabaseHistory, HistoryPagingAdapter.ViewHolder>(
    HISTORY_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val withDataBinding: LayoutHistoryPagingItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ViewHolder.LAYOUT,
            parent,
            false
        )
        return ViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.also {
            it.item = getItem(position)
        }
    }

    class ViewHolder(val binding: LayoutHistoryPagingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.layout_history_paging_item
        }
    }

    companion object {
        private val HISTORY_COMPARATOR = object : DiffUtil.ItemCallback<DatabaseHistory>() {
            override fun areItemsTheSame(oldItem: DatabaseHistory, newItem: DatabaseHistory) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DatabaseHistory, newItem: DatabaseHistory) =
                oldItem == newItem
        }
    }
}