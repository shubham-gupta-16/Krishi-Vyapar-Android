package com.acoder.krishivyapar.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.acoder.krishivyapar.adapters.SubCategoryRecyclerAdapter
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.CategoryManager
import com.acoder.krishivyapar.databinding.FragmentRecyclerBinding


class SubCategoryFragment() : Fragment() {
    private lateinit var binding: FragmentRecyclerBinding;
    private lateinit var adapter: SubCategoryRecyclerAdapter
    private var listener : ((CategoryManager.SubCategoryModel) -> Unit)? = null
    private lateinit var api: Api
    private var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(CATEGORY_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerBinding.inflate(layoutInflater, container, false)
        binding.root.elevation = 1f
        return binding.root
    }

    fun setOnItemClickListener(listener: (model: CategoryManager.SubCategoryModel) -> Unit){
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = Api(requireContext())
        val categoryManager = CategoryManager(requireContext())
        adapter = SubCategoryRecyclerAdapter(requireContext(), categoryManager.getSubCategories(categoryId))
        binding.fragmentRecycler.adapter = adapter
        binding.fragmentRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter.onItemClickListener(listener)
    }

    companion object {
        private const val CATEGORY_PARAM = "param1"
        @JvmStatic
        fun newInstance(categoryId: Int) =
            SubCategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(CATEGORY_PARAM, categoryId)
                }
            }
    }
}