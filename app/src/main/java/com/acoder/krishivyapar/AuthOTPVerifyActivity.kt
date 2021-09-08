package com.acoder.krishivyapar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.ApiData
import com.acoder.krishivyapar.databinding.ActivityOtpverifyBinding
import com.acoder.krishivyapar.utils.PhoneAuthHandler

class AuthOTPVerifyActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOtpverifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mobile = intent.getStringExtra("mobile").toString()
        val phoneAuthHandler = PhoneAuthHandler(this, true)

        binding.mobileText.text = "+91 $mobile"

        binding.otpView.setOtpCompletionListener { otp ->
            phoneAuthHandler.verifyCode(otp, object : PhoneAuthHandler.OnCompleteListener {
                override fun onSuccess(uid: String) {
                    Api(this@AuthOTPVerifyActivity).requestSignUp(mobile, uid).atSuccess { name ->
                        if (name== null)
                            startActivity(Intent(this@AuthOTPVerifyActivity, AuthNameRegisterActivity::class.java))
                        else {
                            startActivity(Intent(this@AuthOTPVerifyActivity, MainActivity::class.java))

                        }
                        finishAffinity()

                    }.atError { code, message ->
                        Toast.makeText(this@AuthOTPVerifyActivity, "$code -> $message", Toast.LENGTH_SHORT).show()
                    }.execute()
                }

                override fun onError(message: String?) {
                    TODO("Not yet implemented")
                }

            })
        }
        phoneAuthHandler.sendOTP("+91$mobile", object : PhoneAuthHandler.OnCodeSentListener {
            override fun onSent() {
            }

            override fun onCodeAutoDetected(code: String) {
                binding.otpView.setText(code)
            }

            override fun onError(message: String) {
                Toast.makeText(this@AuthOTPVerifyActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}