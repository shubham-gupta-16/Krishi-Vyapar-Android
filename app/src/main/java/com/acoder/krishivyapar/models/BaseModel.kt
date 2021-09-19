package com.acoder.krishivyapar.models

import java.text.NumberFormat
import java.util.*

open class BaseModel {
    fun formatPrice(floatPrice: Float): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        val price = format.format(floatPrice)
        return if (price.endsWith(".00")) price.replace(".00", "") else price;
    }
}