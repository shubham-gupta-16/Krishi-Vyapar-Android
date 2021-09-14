package com.acoder.krishivyapar.api

import android.content.Context
import com.acoder.krishivyapar.models.LocationModel
import org.json.JSONObject

open class ApiData(context: Context) : BaseApi(context) {

    fun hasCurrentUser():Boolean{
        return getToken() != null
    }

    fun setLocation(model: LocationModel){
        val editor = sharedPref.edit()
        editor.putString("locText", model.locText)
        editor.putFloat("lat", model.lat)
        editor.putFloat("lng", model.lng)
        editor.apply()
    }

    fun getLocation(): LocationModel? {
        val locText = sharedPref.getString("locText", "").toString()
        val lat = sharedPref.getFloat("lat", 0f)
        val lng = sharedPref.getFloat("lat", 0f)
        if (locText.isEmpty())
            return null
        return LocationModel(locText, lat, lng)
    }

    fun logout(){
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
    fun getName(): String? {
        return sharedPref.getString("name", null)
    }

    protected fun updateName(name: String?) {
        val editor = sharedPref.edit()
        editor.putString("name", name)
        editor.apply()
    }

    protected fun localSignUp(token:String, name:String?){
        val editor = sharedPref.edit()
        editor.putString("token", token)
        editor.putString("name", name)
        editor.apply()
    }

    private fun getToken(): String? {
        return sharedPref.getString("token", null)
    }

//    main api utils *********************
    protected fun JSONObject.tryString(key: String): String? {
        val value = this.optString(key)
        if (value === "null") return null
        return value
    }

    protected fun requestBuilder(path: String): EasyNetwork.Builder {
        return EasyNetwork.Builder(getBaseUrl() + path)
    }

    protected fun authRequestBuilder(path: String): EasyNetwork.Builder {
        val builder = EasyNetwork.Builder(getBaseUrl() + path)
        builder.addHeader("Auth", getToken())
        return builder
    }
}