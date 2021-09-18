package com.acoder.krishivyapar.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class FragmentPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    val viewPager2: ViewPager2 = ViewPager2(context, attrs)
    private val mFragmentList: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    init {
        addView(viewPager2)
    }

    fun build(fragmentManager: FragmentManager, lifecycle: Lifecycle) {
        val adapter = FragmentPagerAdapter(fragmentManager, lifecycle, mFragmentList)
        viewPager2.adapter = adapter
    }

    private class FragmentPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val mFragmentList: List<Fragment>
    ) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getItemCount(): Int {
            return mFragmentList.size
        }
    }
}