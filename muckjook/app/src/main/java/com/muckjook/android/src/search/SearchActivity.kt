package com.muckjook.android.src.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.muckjook.android.R
import com.muckjook.android.databinding.ActivitySearchBinding
import com.muckjook.android.databinding.ActivityShopDetailBinding

class SearchActivity : AppCompatActivity() {

    private var mKeyword:String = ""
    private lateinit var mBinding : ActivitySearchBinding
    private lateinit var mSearchAdapter: SearchAdapter
    private lateinit var mMainViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        //뷰모델 연결
        mMainViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mBinding.viewModel = mMainViewModel

        //뷰모델을 LifeCycle에 종속시킴, LifeCycle 동안 옵저버 역할을 함
        mBinding.lifecycleOwner = this

        /*
        mMainViewModel.searchList.observe(this, Observer {
            mSearchAdapter.setData(it)
        })
         */

    }

    fun search(){

    }

    fun back(){
        finish()
    }

}
