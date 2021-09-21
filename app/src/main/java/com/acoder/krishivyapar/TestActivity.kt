package com.acoder.krishivyapar

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.utils.LocaleHelper
import java.util.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        LocaleHelper.setLocale(this, "en")

//        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ImagesFragment()).commit()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

}