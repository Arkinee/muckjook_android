package com.muckjook.android.src.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muckjook.android.R
import com.muckjook.android.databinding.ActivitySearchBinding
import com.muckjook.android.databinding.ActivityShopDetailBinding

class SearchActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        mBinding.lifecycleOwner = this



    }

    override fun onClick(v: View?) {
        when(v?.id){


        }
    }
}