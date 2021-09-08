package com.acoder.krishivyapar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.acoder.krishivyapar.databinding.FragmentHomeBinding
import com.acoder.krishivyapar.fragments.sub_fragments.HomePostsFragment
import com.acoder.krishivyapar.fragments.sub_fragments.HomeRequestsFragment
import com.acoder.krishivyapar.views.MyViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Requests"))

        setupViewPager(binding.viewpager2)

        TabLayoutMediator(binding.tabLayout, binding.viewpager2
        ) { tab, position ->
            when(position){
                0->            tab.text = "Posts"
                1->            tab.text = "Requests"
            }
        }.attach()


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupViewPager(viewPager2: ViewPager2) {
//        applying fragments by using adapter
        val adapter = MyViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        adapter.addFragment(HomePostsFragment())
        adapter.addFragment(HomeRequestsFragment())
        viewPager2.adapter = adapter
    }
}