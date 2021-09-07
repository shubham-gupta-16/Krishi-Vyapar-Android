package com.acoder.krishivyapar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.ApiData
import com.acoder.krishivyapar.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiData = ApiData(this)

        if (!apiData.hasCurrentUser()) {
            Thread.sleep(2000)
            goToSignUpPage()
        } else {
            Thread.sleep(1000)
            Api.verifyUser(apiData).atSuccess { name ->
                if (name == null)
                    startActivity(Intent(this, AuthNameRegisterActivity::class.java))
                else
                    startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.atError { code, message ->
                if (code == Api.INVALID_TOKEN || code == Api.USER_NOT_EXIST){
                    apiData.logout()
                    goToSignUpPage()
                    return@atError
                }
                // TODO: 9/7/2021 handle user validity error
                Toast.makeText(this, "$code -> $message", Toast.LENGTH_SHORT).show()
            }.execute()

        }

    }

    private fun goToSignUpPage(){
        startActivity(Intent(this, AuthSignUpActivity::class.java))
        finish()
    }
}