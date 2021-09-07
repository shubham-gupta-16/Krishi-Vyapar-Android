package com.acoder.krishivyapar

import android.app.Application
import com.androidnetworking.AndroidNetworking

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext);
    }
}