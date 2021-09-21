package com.acoder.krishivyapar.fragments.main

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.acoder.krishivyapar.AuthSignUpActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.FragmentAccountBinding
import com.shubhamgupta16.materialkit.AnimUtils
import com.shubhamgupta16.materialkit.UtilsKit

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding;
    private lateinit var api: Api
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        api = Api(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()

        binding.logoutButton.setOnClickListener {
            val alert = AlertDialog.Builder(requireActivity())
            alert.setTitle("Logout")
            alert.setMessage("Do your really want to logging out from this device.")

            alert.setPositiveButton(
                "Yes"
            ) { _, _ ->
                api.logout()
                requireActivity().finishAffinity()
                startActivity(Intent(requireContext(), AuthSignUpActivity::class.java))
            }
            alert.setNegativeButton("No", null)
            alert.show()
        }

        binding.editName.setOnClickListener {
            val alert = AlertDialog.Builder(requireActivity())
            val editText = EditText(requireActivity())

            val pDp = UtilsKit.idpToPx(25f, requireActivity())
            editText.setPadding(pDp, pDp, pDp, pDp)
            editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            editText.setText(binding.nameTextAcc.text.toString())
            alert.setTitle("Edit User Name")
            alert.setMessage("Do you really want to change your name.")

            alert.setView(editText)

            alert.setPositiveButton(
                "Done"
            ) { _, _ ->
                AnimUtils.fadeVisibleView(binding.loadingLayout.v)
                val name = editText.text.toString()
                UtilsKit.hideKeyboard(requireActivity())
                api.requestUpdateName(name).atSuccess {
                    updateUI()
                    AnimUtils.fadeHideView(binding.loadingLayout.v)
                }.atError { code, message ->
                    AnimUtils.fadeHideView(binding.loadingLayout.v)
                    Toast.makeText(
                        requireActivity(),
                        "Something went wrong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }.execute()

            }
            alert.setNegativeButton(
                "Cancel"
            ) { _, _ ->
                UtilsKit.hideKeyboard(requireActivity())
            }
            alert.show()
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI()

    }

    private fun updateUI() {
        if (!::binding.isInitialized) return
        binding.nameTextAcc.text = api.getName()
        binding.mobileTextAcc.text = api.getMobile()
    }
}