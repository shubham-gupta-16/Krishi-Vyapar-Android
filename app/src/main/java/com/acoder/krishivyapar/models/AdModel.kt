package com.acoder.krishivyapar.models

import com.acoder.krishivyapar.api.parseImages
import com.acoder.krishivyapar.api.parseLocation
import com.acoder.krishivyapar.api.parseUser
import com.acoder.krishivyapar.api.tryString
import org.json.JSONObject

data class AdModel(
    var id:Int, var title: String, var description: String?, var price: Float, var quantity: Float, var unit: String,
    var categoryId: Int, var subCategoryId: Int, var extras: String?, var location: LocationModel,
    var images : List<ImageModel>, val user: UserModel,
    val postId: Int = 0, val viewsCount: Int = 0, val favCount: Int = 0, val createdAt: String = "",
    val status: Int = 0,
): BaseModel() {
    companion object {
        fun newInstance(ad: JSONObject): AdModel{
            return AdModel(
                ad.optInt("id"),
                ad.optString("title"),
                ad.tryString("des"),
                ad.optDouble("price").toFloat(),
                ad.optDouble("quantity").toFloat(),
                ad.optString("unit"),
                ad.optInt("categoryId"),
                ad.optInt("subCategoryId"),
                ad.getString("extras"),
                parseLocation(ad.getJSONObject("location")),
                parseImages(ad.getJSONArray("images")),
                parseUser(ad.getJSONObject("user")),
                ad.getInt("id"),
                ad.getInt("views"),
                ad.getInt("fav"),
                ad.optString("createdAt"),
                ad.getInt("status"),
            )
        }
    }
}
