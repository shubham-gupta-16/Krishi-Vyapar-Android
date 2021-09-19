package com.acoder.krishivyapar.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

open class BaseApi {
    companion object {

        private const val prefName = "zombiePrefer"

        fun setUrl(context: Context, url: String) {
            val sharedPref: SharedPreferences =
                context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("baseUrl", url)
            Log.d("setBaseUrl", url)
            editor.apply()
        }

        fun getUrl(context: Context): String {
            val sharedPref: SharedPreferences =
                context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.getString("baseUrl", "").toString()
        }

        fun getImagesUrl(context: Context, path: String = ""): String {
            return BaseApi.getUrl(context) +"images/"+path
        }

        fun initBaseUrl(context: Context, url: String) {
            if (getUrl(context).isEmpty())
                setUrl(context, url)
        }
    }

}