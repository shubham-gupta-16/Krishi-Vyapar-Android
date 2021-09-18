package com.acoder.krishivyapar.fragments.main.sub_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.acoder.krishivyapar.adapters.AdRecyclerAdapter
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentHomeMarketBinding
import com.acoder.krishivyapar.models.AdModel


class HomeMarketFragment() : Fragment() {
    private lateinit var binding: FragmentHomeMarketBinding;
    private val list = ArrayList<AdModel>()
    private lateinit var adapter: AdRecyclerAdapter
    private lateinit var api: Api
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMarketBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = Api(requireContext())
        adapter = AdRecyclerAdapter(requireContext(), list)
        binding.adsRecycler.adapter = adapter
        binding.adsRecycler.layoutManager = LinearLayoutManager(requireContext())
        requestAds()
    }

    private fun requestAds(page: Int = 1){
        if (page == 1)
            list.clear()

        api.requestAds(page).atSuccess { locationList ->
            list.addAll(locationList)
            adapter.notifyItemRangeChanged(0, list.size)
        }.atError { code, message ->

        }.execute()
    }
}