package com.muckjook.android.src.shop

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.muckjook.android.R
import com.muckjook.android.databinding.ActivityShopDetailBinding

class ShopActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityShopDetailBinding
    private val model: ShopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop_detail)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = model


    }

}