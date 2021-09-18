package com.acoder.krishivyapar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.acoder.krishivyapar.fragments.add.ImagesFragment

class TestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ImagesFragment()).commit()
//        startActivity(Intent(this, SplashActivity::class.java))
    }

}