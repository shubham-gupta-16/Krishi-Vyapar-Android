package com.acoder.krishivyapar.models

import com.acoder.krishivyapar.api.parseImages
import com.acoder.krishivyapar.api.parseLocation
import com.acoder.krishivyapar.api.parseUser
import com.acoder.krishivyapar.api.tryString
import org.json.JSONObject

data class AdListModel(
    var id: Int, var title: String, var price: Float, var quantity: Float, var unit: String,
    var locText: String,var createdAt: String = "",
    var image : ImageModel,


): BaseModel(){
    fun getTrimmedLocation(): String {
        if (!locText.contains(","))
            return locText
        val arr = locText.split(",")
        return "${arr[0]},${arr[1]}"
    }
    companion object {
        fun newInstance(ad: JSONObject): AdListModel{
            return AdListModel(
                ad.optInt("id"),
                ad.optString("title"),
                ad.optDouble("price").toFloat(),
                ad.optDouble("quantity").toFloat(),
                ad.optString("unit"),
                ad.getString("locText"),
                ad.optString("createdAt"),
                ImageModel.newInstance(ad.getJSONObject("image")),
            )
        }
    }
}
