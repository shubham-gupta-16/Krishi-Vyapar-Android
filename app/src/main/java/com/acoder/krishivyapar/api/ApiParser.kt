package com.acoder.krishivyapar.api

import android.util.Log
import com.acoder.krishivyapar.models.AdModel
import com.acoder.krishivyapar.models.LocationModel
import com.acoder.krishivyapar.models.UserModel
import org.json.JSONObject

private fun JSONObject.tryString(key: String): String? {
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
    return UserModel(user.getString("uid"), user.getString("name"), user.tryString("profileUrl"));
}

fun parseAd(ad: JSONObject): AdModel{
    return AdModel(
        ad.getInt("id"),
        ad.optString("title"),
        ad.tryString("des"),
        ad.optDouble("price").toFloat(),
        ad.optString("category"),
        ad.tryString("subCategory"),
        ad.getInt("views"),
        ad.getInt("fav"),
        ad.optString("createdAt"),
        ad.getInt("status"),
        ad.getString("extras"),
        parseUser(ad.getJSONObject("user")),
        parseLocation(ad.getJSONObject("location"))
    )
}