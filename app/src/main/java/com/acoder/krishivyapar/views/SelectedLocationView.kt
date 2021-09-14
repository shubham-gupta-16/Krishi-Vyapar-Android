package com.acoder.krishivyapar.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.acoder.krishivyapar.R

class SelectedLocationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_location, this)
    }

    fun setText(text: String){
        findViewById<TextView>(R.id.loc_text).text = text
    }
}