package com.acoder.krishivyapar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.ActivityAddNewAdBinding
import com.acoder.krishivyapar.fragments.add.*
import com.acoder.krishivyapar.views.ViewCollection

class AddNewAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewAdBinding
    private var currentFrag = 0
    private val adMap = hashMapOf<String,String>()
//    private val adModel = AdModel("", "", 0f, 0f,"", 0, 0, null, LocationModel("", 0f,0f), null)
    private var images:Map<String, Int>? = null

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
            adMap["categoryId"] = model.categoryId.toString()
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
            adMap["subCategoryId"] = model.subCategoryId.toString()
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
        adRelevantDetailsFragment.setOnItemClickListener { title, desc, price, quantity, unit, extras ->
            adMap["title"] = title
            adMap["desc"] = desc
            adMap["price"] = price.toString()
            adMap["quantity"] = quantity.toString()
            adMap["unit"] = unit
// TODO: 9/18/2021 MAYBE USE JSON in MODEL
            adMap["extras"] = extras.toString()
            addImages()
        }
        supportFragmentManager.beginTransaction()
            .stackAnimate()
            .translate(adRelevantDetailsFragment)
            .addToBackStack(null).commit()
        ++currentFrag
        ++currentFrag
    }

    private fun addFinalFrag() {
        val finalFragment = FinalFragment()
        finalFragment.setOnPostClickListener { locText ->
            startUploading(locText)
        }
        supportFragmentManager.beginTransaction()
            .stackAnimate()
            .translate(finalFragment)
            .addToBackStack(null).commit()
        ++currentFrag
        ++currentFrag
    }

    private fun startUploading(locText: String) {
        val api = Api(this)
        val loadingProgressDialog = ViewCollection.LoadingProgressDialog(this, "Posting")
        loadingProgressDialog.show()
        if (images == null)
            return
        api.requestUploadAd(this, adMap, images!!).atSuccess {
            loadingProgressDialog.dismiss()
            Toast.makeText(this, "Ad Posted Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }.setUploadProgressListener { byteUploaded, totalBytes ->
            loadingProgressDialog.updateProgress(byteUploaded, totalBytes)
        }.atError { code, message ->
            loadingProgressDialog.dismiss()
        }.execute()
    }

    private fun addImages() {
        val addImages = ImagesFragment()
        addImages.setOnNextClickListener { images ->
            this.images = images
            addFinalFrag()
        }
        supportFragmentManager.beginTransaction()
            .stackAnimate()
            .translate(addImages)
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
            3 -> "Choose Images"
            4 -> "Your Details"
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