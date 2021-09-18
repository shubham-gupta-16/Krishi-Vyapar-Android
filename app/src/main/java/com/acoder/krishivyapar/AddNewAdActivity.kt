package com.acoder.krishivyapar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.acoder.krishivyapar.databinding.ActivityAddNewAdBinding
import com.acoder.krishivyapar.fragments.add.AdRelevantDetailsFragment
import com.acoder.krishivyapar.fragments.add.ChooseCategoryFragment
import com.acoder.krishivyapar.fragments.add.SubCategoryFragment

class AddNewAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewAdBinding
    private var currentFrag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        updateToolbar()

        val categoryFragment = ChooseCategoryFragment()
        categoryFragment.setOnItemClickListener { model ->
            chooseSubCategory(model.categoryId)
        }
        supportFragmentManager.beginTransaction().translate(categoryFragment).commit()

        supportFragmentManager.addOnBackStackChangedListener {
            --currentFrag
            updateToolbar()
        }
    }

    private fun chooseSubCategory(categoryId: Int) {
        val subCategoryFragment = SubCategoryFragment.newInstance(categoryId)
        subCategoryFragment.setOnItemClickListener { model ->
            addRelevantDetails()
        }
        supportFragmentManager.beginTransaction()
            .stackAnimate()
            .translate(subCategoryFragment)
            .addToBackStack(null).commit()
        ++currentFrag
        ++currentFrag
    }

    private fun addRelevantDetails() {
        val adRelevantDetailsFragment = AdRelevantDetailsFragment()
        adRelevantDetailsFragment.setOnItemClickListener { model ->

        }
        supportFragmentManager.beginTransaction()
            .stackAnimate()
            .translate(adRelevantDetailsFragment)
            .addToBackStack(null).commit()
        ++currentFrag
        ++currentFrag
    }

//    utils related to activity UI update

    private fun updateToolbar() {
        if (currentFrag == 0)
            binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24)
        else
            binding.toolbar.setNavigationIcon(R.drawable.ic_kit_arrow_back_24)

        val title = when (currentFrag) {
            0 -> "Choose Category"
            1 -> "Choose Sub-Category"
            2 -> "Provide Details"
            else -> ""
        }
        binding.toolbar.title = title
    }

    private fun FragmentTransaction.stackAnimate(): FragmentTransaction {
        return this.setCustomAnimations(
            R.anim.slide_in_enter,
            R.anim.slide_out_enter,
            R.anim.slide_in_exit,
            R.anim.slide_out_exit,
        )
    }

    private fun FragmentTransaction.translate(fragment: Fragment): FragmentTransaction {
        return this.replace(binding.fragmentFrame.id, fragment)
    }
}