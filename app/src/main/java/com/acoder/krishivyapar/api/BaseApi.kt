package com.acoder.krishivyapar.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

open class BaseApi(protected val context: Context) {
    private val prefName = "zombiePref30"
    internal var sharedPref: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    protected fun setBaseUrl(url:String){
        val editor = sharedPref.edit()
        editor.putString("baseUrl", url)
        Log.d("setBaseUrl", url)
        editor.apply()
    }

    protected fun getBaseUrl(): String {
        return sharedPref.getString("baseUrl", "").toString()
    }

    fun initBaseUrl(url:String){
        if (getBaseUrl().isEmpty())
            setBaseUrl(url)
    }
}