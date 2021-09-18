package com.acoder.krishivyapar.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.acoder.krishivyapar.adapters.CategoryRecyclerAdapter
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.CategoryManager
import com.acoder.krishivyapar.databinding.FragmentAdRelevantDetailsBinding
import com.acoder.krishivyapar.databinding.FragmentRecyclerBinding


class AdRelevantDetailsFragment() : Fragment() {
    private lateinit var binding: FragmentAdRelevantDetailsBinding;
    private lateinit var api: Api
    private var listener : ((CategoryManager.CategoryModel) -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdRelevantDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun setOnItemClickListener(listener: (model: CategoryManager.CategoryModel) -> Unit){
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = Api(requireContext())
    }

}