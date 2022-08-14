package com.muckjook.android.src.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muckjook.android.src.search.model.SearchShop

class SearchViewModel : ViewModel() {
    private val _searchList = MutableLiveData<ArrayList<SearchShop>>()
    val searchList : LiveData<ArrayList<SearchShop>>
        get() = _searchList

    private var items = ArrayList<SearchShop>()

}