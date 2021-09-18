package com.acoder.krishivyapar.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.acoder.krishivyapar.LocationActivity
import com.acoder.krishivyapar.SearchActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentHomeBinding
import com.acoder.krishivyapar.fragments.main.sub_fragments.HomeMarketFragment
import com.acoder.krishivyapar.fragments.main.sub_fragments.HomeRequestsFragment
import com.acoder.krishivyapar.adapters.MyViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding;
    lateinit var api : Api;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        api = Api(requireContext())
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Requests"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"))

        setupViewPager(binding.viewpager2)

        TabLayoutMediator(binding.tabLayout, binding.viewpager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Market"
                1 -> tab.text = "Requests"
            }
        }.attach()


        binding.selectedLocationView.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }
//        search Button anim
        binding.searchButton.setOnClickListener{
            binding.searchButton.playAnimation()
            startActivity(Intent(requireContext(), SearchActivity::class.java))
//            AnimUtils.circleReveal(binding.searchLayout, binding.searchLayout.width, 0)
        }
    }

    override fun onStart() {
        super.onStart()
        val selectedLocation = api.getLocation()
        if (selectedLocation == null){
            startActivity(Intent(requireContext(), LocationActivity::class.java))
            return
        }
        binding.selectedLocationView.setText(selectedLocation.getTrimmed())
    }

    private fun setupViewPager(fragmentPager: ViewPager2) {
        val adapter = MyViewPagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(HomeMarketFragment())
        adapter.addFragment(HomeRequestsFragment())
        fragmentPager.adapter = adapter
    }
}