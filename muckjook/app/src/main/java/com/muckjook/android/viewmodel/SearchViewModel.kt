package com.muckjook.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.muckjook.android.base.BaseViewModel
import com.muckjook.android.widget.utils.ScreenState
import com.muckjook.android.widget.utils.SingleLiveEvent
import com.muckjook.domain.model.StoreResponse
import com.muckjook.domain.usecase.GetStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchShopUseCase: GetStoreUseCase
): BaseViewModel() {
    val searchList: LiveData<List<StoreResponse>> get() = _searchList
    private val _searchList = SingleLiveEvent<List<StoreResponse>>()

    fun getSearchShop(owner: String) = viewModelScope.launch {
        val response = getSearchShopUseCase.execute(this@SearchViewModel, owner)
        if (response == null) mutableScreenState.postValue(ScreenState.ERROR) else{
            mutableScreenState.postValue(ScreenState.RENDER)
            _searchList.postValue(response!!)
        }
    }

}