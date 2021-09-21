package com.acoder.krishivyapar.api

import android.util.Log
import com.acoder.krishivyapar.models.AdModel
import com.acoder.krishivyapar.models.ImageModel
import com.acoder.krishivyapar.models.LocationModel
import com.acoder.krishivyapar.models.UserModel
import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.tryString(key: String): String? {
    val value = this.optString(key)
    if (value === "null") return null
    return value
}
fun parseLocation(each: JSONObject): LocationModel {
    val locText = each.optString("locText")
    Log.d("parser", locText)
    val lat = each.optDouble("lat")
    val lng = each.optDouble("lng")
    return LocationModel(locText, lat.toFloat(), lng.toFloat())
}

fun parseUser(user: JSONObject): UserModel {
    return UserModel(user.getString("uid"), user.getString("name"), user.getString("mobile"), user.tryString("profileUrl"));
}

fun parseImages(images: JSONArray): ArrayList<ImageModel?> {
    val list = arrayListOf<ImageModel?>()
    for (i in 0 until images.length()){
        list.add(ImageModel.newInstance(images.getJSONObject(i)))
    }
    return list
}