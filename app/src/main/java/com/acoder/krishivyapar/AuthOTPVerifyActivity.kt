package com.acoder.krishivyapar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.ApiData
import com.acoder.krishivyapar.databinding.ActivityOtpverifyBinding
import com.acoder.krishivyapar.utils.PhoneAuthHandler
import com.shubhamgupta16.materialkit.AnimUtils

class AuthOTPVerifyActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOtpverifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mobile = intent.getStringExtra("mobile").toString()
        val phoneAuthHandler = PhoneAuthHandler(this)

        binding.mobileText.text = "+91 $mobile"

        binding.firstPinView.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (s.length >= 6){
                        AnimUtils.fadeVisibleView(binding.loadingLayout.v)
                        phoneAuthHandler.verifyCode(s.toString(), object : PhoneAuthHandler.OnCompleteListener {
                            override fun onSuccess(uid: String) {
                                Api(this@AuthOTPVerifyActivity).requestSignUp(mobile, uid).atSuccess { name ->
                                    if (name== null)
                                        startActivity(Intent(this@AuthOTPVerifyActivity, AuthNameRegisterActivity::class.java))
                                    else {
                                        startActivity(Intent(this@AuthOTPVerifyActivity, MainActivity::class.java))
                                    }
                                    finishAffinity()
                                }.atError { code, message ->
                                    AnimUtils.fadeHideView(binding.loadingLayout.v)
                                    Toast.makeText(this@AuthOTPVerifyActivity, message, Toast.LENGTH_SHORT).show()
                                }.execute()
                            }

                            override fun onError(message: String?) {
                                if (message != null)
                                Toast.makeText(this@AuthOTPVerifyActivity, message, Toast.LENGTH_SHORT)
                                    .show()
                                AnimUtils.fadeHideView(binding.loadingLayout.v)
                            }
                        })
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        phoneAuthHandler.sendOTP("+91$mobile", object : PhoneAuthHandler.OnCodeSentListener {
            override fun onSent() {
            }

            override fun onCodeAutoDetected(code: String) {
                binding.firstPinView.setText(code)
            }

            override fun onError(message: String) {
                Toast.makeText(this@AuthOTPVerifyActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}