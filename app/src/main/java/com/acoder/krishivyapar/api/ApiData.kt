package com.acoder.krishivyapar.api

import android.content.Context
import android.content.SharedPreferences

open class ApiData(context: Context) {
    private var sharedPreferences: SharedPreferences
    private val prefName = "zombiePref30"

    init {
        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    internal fun localSignUp(token:String, name:String?){
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putString("name", name)
        editor.apply()
    }

    fun hasCurrentUser():Boolean{
        return getToken() != null
    }

    fun logout(){
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.remove("name")
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun getName(): String? {
        return sharedPreferences.getString("name", null)
    }

    fun updateName(name: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.apply()
    }
}