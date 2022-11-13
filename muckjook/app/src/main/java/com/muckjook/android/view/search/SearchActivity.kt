package com.muckjook.android.view.search

import android.view.View
import androidx.activity.viewModels
import com.muckjook.android.R
import com.muckjook.android.base.BaseActivity
import com.muckjook.android.databinding.ActivitySearchBinding
import com.muckjook.android.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity: BaseActivity<ActivitySearchBinding>(R.layout.activity_search){

    private var mKeyword:String = ""
    private lateinit var mSearchAdapter: SearchAdapter
    private val mMainViewModel by viewModels<SearchViewModel>()

    override fun init(){
        binding.viewModel = this
        observeViewmodels()

    }

    //검색버튼 클릭
    fun search(view: View){

    }

    fun observeViewmodels(){
        mMainViewModel

    }

}
