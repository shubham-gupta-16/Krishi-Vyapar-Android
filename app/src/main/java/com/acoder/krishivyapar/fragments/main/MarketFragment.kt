package com.acoder.krishivyapar.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentHomeBinding
import com.acoder.krishivyapar.databinding.FragmentHomeMarketBinding
import com.acoder.krishivyapar.databinding.FragmentMarketBinding

class MarketFragment() : Fragment() {

    lateinit var binding: FragmentMarketBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}