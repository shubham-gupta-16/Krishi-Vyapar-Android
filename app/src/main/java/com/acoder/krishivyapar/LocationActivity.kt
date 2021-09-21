package com.acoder.krishivyapar

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.ActivityLocationBinding
import com.acoder.krishivyapar.models.LocationModel
import com.shubhamgupta16.materialkit.AnimUtils

class LocationActivity : AppCompatActivity() {
    lateinit var binding: ActivityLocationBinding
    lateinit var api: Api
    var locationList = arrayListOf<LocationModel>()
    var key = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = Api(this)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return false
                if (newText.length < 4) return false
                key++
                binding.testSuggestion.filterSuggestion(newText, null)
                fetchLocations(newText, key)
                return false
            }
        })

        binding.testSuggestion.initialize("locations")
        binding.testSuggestion.setCopyIcon(android.R.color.transparent)
        binding.testSuggestion.setSuggestionIcon(R.drawable.ic_loc_icon)
        binding.testSuggestion.setOnSuggestionClickListener { suggestion, listPosition, _, isHistory ->
            if (!isHistory)
                binding.testSuggestion.addHistory(suggestion)
            if (listPosition >= 0){
                Log.d("locationSelected", locationList[listPosition].toString())
                api.setLocation(locationList[listPosition])
                finish()
            } else {
                fetchLocations(suggestion, 0, true)
            }
        }
    }

    private fun fetchLocations(newText: String, k: Int, toSet: Boolean = false) {
        if (toSet)
            AnimUtils.fadeVisibleView(binding.loadingLayout.v)
        api.fetchLocations(newText).atSuccess { list ->
            if (toSet){
                api.setLocation(list[0])
                finish()
                return@atSuccess
            }
            if (k != key) return@atSuccess
            locationList = list
            val sugList = ArrayList<String>()
            for (s in list)
                sugList.add(s.locText)
            binding.testSuggestion.filterSuggestion(newText, sugList)
        }.execute()
    }
}