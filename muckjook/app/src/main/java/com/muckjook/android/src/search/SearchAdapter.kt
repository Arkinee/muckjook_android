package com.muckjook.android.src.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.muckjook.android.R

class SearchAdapter(context:Context): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var dataList = mutableListOf<SearchShop>()
    inner class SearchViewHolder(private val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(shop:SearchShop){
            binding.
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_search_result, parent, false)
        return SearchViewHolder(binding)
    }

}