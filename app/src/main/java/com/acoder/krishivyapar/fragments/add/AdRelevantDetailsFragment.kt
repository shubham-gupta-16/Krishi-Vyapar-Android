package com.acoder.krishivyapar.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentAdRelevantDetailsBinding
import org.json.JSONObject


class AdRelevantDetailsFragment() : Fragment() {
    private lateinit var binding: FragmentAdRelevantDetailsBinding
    private lateinit var api: Api
    private var listener: ((name: String, desc: String, price: Float, quantity: Float, unit: String, extras: JSONObject) -> Unit)? =
        null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized)
            binding = FragmentAdRelevantDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    fun setOnItemClickListener(listener: (name: String, desc: String, price: Float, quantity: Float, unit: String, extras: JSONObject) -> Unit) {
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = Api(requireContext())

        val languages = resources.getStringArray(R.array.unit)
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_activated_1, languages)
        binding.unitAutoDrop.setAdapter(arrayAdapter)

        binding.nextButton.setOnClickListener {
            val name = binding.inputName.editText?.text.toString()
            val quantity = binding.inputQuantity.editText?.text.toString()
            val unit = binding.unitAutoDrop.text.toString()
            val price = binding.inputPrice.editText?.text.toString()
            val desc = binding.inputDesc.editText?.text.toString()

            val validate = validateForm(name, quantity, unit, price, desc)
            if (validate)
                listener?.invoke(
                    name,
                    desc,
                    price.toFloat(),
                    quantity.toFloat(),
                    unit,
                    JSONObject()
                )
        }
    }

    private fun validateForm(
        name: String,
        quantity: String,
        unit: String,
        price: String,
        desc: String
    ): Boolean {
        var noError = true
        binding.inputName.isErrorEnabled = false
        binding.inputQuantity.isErrorEnabled = false
        binding.inputUnit.isErrorEnabled = false
        binding.inputPrice.isErrorEnabled = false
        if (name.isEmpty()) {
            binding.inputName.error = "Enter Name"
            noError = false
        }
        if (quantity.isEmpty()) {
            binding.inputQuantity.error = "Enter Quantity"
            noError = false
        }
        if (unit.isEmpty()) {
            binding.inputUnit.error = "Select Unit"
            noError = false
        }
        if (price.isEmpty()) {
            binding.inputPrice.error = "Enter Price"
            noError = false
        }
        return noError
    }

}