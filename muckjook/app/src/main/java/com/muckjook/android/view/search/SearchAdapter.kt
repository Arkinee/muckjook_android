package com.muckjook.android.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muckjook.android.databinding.ItemSearchResultBinding
import com.muckjook.domain.model.SearchShop

class SearchAdapter() :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var searchList = ArrayList<SearchShop>()

    // 어떤 xml으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int = searchList.size

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchList[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setItems(items: ArrayList<SearchShop>){
        this.searchList.clear()
        this.searchList.addAll(items)
        notifyDataSetChanged()
    }

    // 생성된 뷰 홀더에 값 지정
    class SearchViewHolder(val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchShop: SearchShop) {
            binding.shop = searchShop
        }

    }

}