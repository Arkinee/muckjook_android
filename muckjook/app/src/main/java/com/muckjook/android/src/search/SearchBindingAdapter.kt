package com.muckjook.android.src.search

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.muckjook.android.R
import com.muckjook.android.src.search.model.SearchShop

object SearchBindingAdapter {
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: ArrayList<SearchShop>) {

        if (recyclerView.adapter == null) {
            val adapter = SearchAdapter()
            adapter.setHasStableIds(true)
            recyclerView.adapter = adapter
        }

        val myAdapter = recyclerView.adapter as SearchAdapter

        myAdapter.searchList = items
        myAdapter.notifyDataSetChanged()
    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(imgView: ImageView, imgUrl: String) {
        Glide.with(imgView.context)
            .load(imgUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_place_holder))
            .into(imgView)
    }

}