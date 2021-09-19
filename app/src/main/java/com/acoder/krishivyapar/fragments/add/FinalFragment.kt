package com.acoder.krishivyapar.fragments.add

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acoder.krishivyapar.LocationActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentFianlBinding


class FinalFragment() : Fragment() {
    private lateinit var binding: FragmentFianlBinding
    private lateinit var api: Api
    private var listener: ((locText: String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized)
            binding = FragmentFianlBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun setOnPostClickListener(listener: (locText: String) -> Unit) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = Api(requireContext())

        binding.inputLocation.setOnClickListener {
            startActivity(Intent(requireContext(), LocationActivity::class.java))
        }

        binding.nextButton.setOnClickListener {
            if (binding.actvLocation.text.toString().isNotEmpty())
                listener?.invoke(binding.actvLocation.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val location = Api(requireContext()).getLocation()
        binding.actvLocation.setText(location?.getTrimmed())
    }
}