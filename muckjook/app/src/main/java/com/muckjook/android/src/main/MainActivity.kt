package com.muckjook.android.src.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.muckjook.android.R
import com.muckjook.android.databinding.ActivityMainBinding
import com.muckjook.android.src.main.adapters.MainViewPagerAdapter
import com.muckjook.android.src.main.fragments.map.MapFragment
import com.muckjook.android.src.main.fragments.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    val mContext = this
    var mMenuIndex = 0

    private lateinit var mBinding: ActivityMainBinding
    private val mMainModel: MainViewModel by viewModels()

    private lateinit var mMainViewPager: ViewPager2
    private lateinit var mAdapter: MainViewPagerAdapter

    private lateinit var mMainMenuIvHome: ImageView
    private lateinit var mMainMenuIvMyPage: ImageView
    private lateinit var mMainTvTop: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.main = this@MainActivity

        mMainMenuIvHome = findViewById(R.id.main_iv_menu_home)
        mMainMenuIvMyPage = findViewById(R.id.main_iv_menu_mypage)
        mMainTvTop = findViewById(R.id.main_tv_top)

        mMainViewPager = findViewById(R.id.main_view_pager)
        mMainViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mMainViewPager.isUserInputEnabled = false

        mAdapter = MainViewPagerAdapter(mContext, supportFragmentManager, lifecycle)
        mAdapter.addFragment(MapFragment(mContext))
        mAdapter.addFragment(MyPageFragment())
        mMainViewPager.adapter = mAdapter

    }

    fun homeClick(){
        mMenuIndex = 0
        mMainViewPager.currentItem = 0

        mMainMenuIvHome.setImageResource(R.drawable.ic_home)
        mMainMenuIvMyPage.setImageResource(R.drawable.ic_mypage_normal)
        mMainTvTop.setText(R.string.home_tv_top)

    }

    fun mypageClick(){
        mMenuIndex = 1
        mMainViewPager.currentItem = 1

        mMainMenuIvHome.setImageResource(R.drawable.icon_home_normal)
        mMainMenuIvMyPage.setImageResource(R.drawable.ic_mypage)
        mMainTvTop.setText(R.string.mypage_tv_top)
    }
}