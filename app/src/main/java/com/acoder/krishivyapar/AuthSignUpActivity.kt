package com.acoder.krishivyapar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.databinding.ActivitySignUpBinding


class AuthSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueButton.setOnClickListener(View.OnClickListener {
            Log.d("tag", "onCreate: checked")
            val mobile = binding.mobileEditText.text.toString()
            if (mobile.length < 10) {
                binding.mobileTextInput.error = "Please enter a valid mobile no."
                return@OnClickListener
            }
            goToOTPVerifyPage(mobile)
        })
    }

    private fun userExist(mobile: String) {
        Api(this).requestUserExist(mobile).atSuccess { name, uid ->
            goToOTPVerifyPage(mobile, name, uid)
        }.atError { code, message ->
            if (code == Api.USER_NOT_EXIST)
                goToOTPVerifyPage(mobile)
            else
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }.execute()
    }

    private fun goToOTPVerifyPage(mobile: String, name: String? = null, uid: String? = null) {
        val i = Intent(this, AuthOTPVerifyActivity::class.java)
        i.putExtra("mobile", mobile)
        if (name != null)
            i.putExtra("name", name)
        if (uid != null)
            i.putExtra("uid", uid)

        startActivity(i)
    }
}