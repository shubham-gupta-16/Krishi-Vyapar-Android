package com.acoder.krishivyapar.fragments.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.acoder.krishivyapar.LocationActivity
import com.acoder.krishivyapar.SearchActivity
import com.acoder.krishivyapar.adapters.MyViewPagerAdapter
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentHomeBinding
import com.acoder.krishivyapar.fragments.main.sub_fragments.HomeMarketFragment
import com.acoder.krishivyapar.fragments.main.sub_fragments.HomeRequestsFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var api: Api;
    private lateinit var adapter: MyViewPagerAdapter
    private lateinit var hmf: HomeMarketFragment
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent = result.data ?: return@registerForActivityResult
                val query = data.getStringExtra("q") ?: return@registerForActivityResult
                fetchSearch(query)
            }
        }
        super.onCreate(savedInstanceState)
    }

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
        binding.selectedLocationIcon.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }
        binding.arrowBack.setOnClickListener {
            fetchSearch(null)
        }
//        search Button anim
        binding.searchButton.setOnClickListener {
            binding.searchButton.playAnimation()

            resultLauncher.launch(Intent(requireContext(), SearchActivity::class.java))
//            AnimUtils.circleReveal(binding.searchLayout, binding.searchLayout.width, 0)
        }
    }

    private fun fetchSearch(query: String?) {
        if (query == null) {
            binding.selectedLocationView.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
            binding.searchText.text = ""
        } else {
            binding.selectedLocationView.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
            binding.searchText.text = query
        }
        this.query = query
        hmf.query(query)
    }

    override fun onStart() {
        super.onStart()
        hmf.query(query)
        val selectedLocation = api.getLocation()
        if (selectedLocation == null) {
            startActivity(Intent(requireContext(), LocationActivity::class.java))
            return
        }
        binding.selectedLocationView.setText(selectedLocation.getTrimmed())
    }

    private fun setupViewPager(fragmentPager: ViewPager2) {
        hmf = HomeMarketFragment()
        adapter = MyViewPagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(hmf)
        adapter.addFragment(HomeRequestsFragment())
        fragmentPager.adapter = adapter
//        yahoo remove isUserInputEnabled
        fragmentPager.isUserInputEnabled = false
    }
}