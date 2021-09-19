package com.acoder.krishivyapar.models

import org.json.JSONArray
import org.json.JSONObject

data class ImageModel(var name: String, var table:String, var resolutions: List<String>){

    fun getPath(resolution: String = ""): String {
        return if (resolutions.contains(resolution))
            "$table/$resolution/$name"
        else
            "$table/$name"
    }

    companion object {
        fun newInstance(image: JSONObject): ImageModel{
            val resArr = image.getJSONArray("resolutions")
            val resolutions = arrayListOf<String>()
            for (j in 0 until resArr.length())
                resolutions.add(resArr.getString(j))
            return ImageModel(image.optString("name"), image.optString("table"), resolutions)
        }
    }

}
