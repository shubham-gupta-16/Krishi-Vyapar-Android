package com.acoder.krishivyapar.fragments.main.sub_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.adapters.AdRecyclerAdapter
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentHomeMarketBinding
import com.acoder.krishivyapar.models.AdListModel


class HomeMarketFragment() : Fragment() {
    private lateinit var binding: FragmentHomeMarketBinding;
    private val list = ArrayList<AdListModel>()
    private lateinit var adapter: AdRecyclerAdapter
    private lateinit var api: Api
    private var query: String? = null
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
        binding.adsRecycler.setLoadingView(R.layout.loading_layout)
        binding.adsRecycler.setLayout(LinearLayout.VERTICAL) { page ->
            requestAds(page)
        }
        requestAds()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun requestAds(page: Int = 1){
        binding.adsRecycler.fetchStart()
        if (page == 1)
            list.clear()

        api.requestAds(page, query).atSuccess { locationList, maxPage, _ ->
            list.addAll(locationList)
//            todo pages
            binding.adsRecycler.dataFetched(true, maxPage, page)
            adapter.notifyDataSetChanged()
        }.atError { code, message ->
            binding.adsRecycler.dataFetched(false, 0, 0)
        }.execute()
    }

    fun query(query: String?) {
        if (::binding.isInitialized) {
            this.query = query
            requestAds()
        }
    }
}