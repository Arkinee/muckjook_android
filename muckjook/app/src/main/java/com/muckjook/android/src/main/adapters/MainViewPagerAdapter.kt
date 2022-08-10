package com.muckjook.android.src.main.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.muckjook.android.src.main.fragments.map.MapFragment
import com.muckjook.android.src.main.fragments.mypage.MyPageFragment

class MainViewPagerAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val mContext = context
    var fragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MapFragment(mContext)
            1 -> return MyPageFragment()
            else -> return MapFragment(mContext)
        }
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    fun removeFragment() {
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }

}