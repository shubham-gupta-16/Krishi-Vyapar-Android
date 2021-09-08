package com.acoder.krishivyapar.views

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment

class ViewPagerAd: PagerAdapter() {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return false
    }
}

class MyViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }
}