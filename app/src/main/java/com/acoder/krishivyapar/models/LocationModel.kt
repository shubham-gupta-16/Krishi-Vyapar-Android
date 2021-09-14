package com.acoder.krishivyapar.models

data class LocationModel(val locText: String, val lat: Float, val lng: Float) {
    fun getTrimmed(): String {
        if (!locText.contains(","))
            return locText
        val arr = locText.split(",")
        return "${arr[0]},${arr[1]}"
    }
}
