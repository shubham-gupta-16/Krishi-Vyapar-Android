package com.acoder.krishivyapar.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acoder.krishivyapar.FullAdViewActivity
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.api.BaseApi
import com.acoder.krishivyapar.databinding.ItemAdHorizontalBinding
import com.acoder.krishivyapar.models.AdListModel
import com.bumptech.glide.Glide
import com.shubhamgupta16.materialkit.UtilsKit
import kotlin.math.roundToInt

class AdRecyclerAdapter(private val context: Context, private val list: List<AdListModel>) :
    RecyclerView.Adapter<AdRecyclerAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val bind = ItemAdHorizontalBinding.inflate(LayoutInflater.from(context))
        return ItemHolder(bind)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (position == 0)
            holder.itemView.setPaddingRelative(0, UtilsKit.idpToPx(8f, context), 0, 0)
        else if (position == list.size - 1)
            holder.itemView.setPaddingRelative(0, 0, 0, UtilsKit.idpToPx(27f, context))
        else
            holder.itemView.setPaddingRelative(0, 0, 0, 0)
        val model = list[position]
//        todo original to thumb
        if (model.image != null)
            Glide.with(context).load(BaseApi.getImagesUrl(context) + model.image!!.getPath("144"))
                .placeholder(R.drawable.placeholder_loading_image).error(R.drawable.placeholder_loading_image).into(holder.bind.image)
        holder.bind.price.text = model.formatPrice(model.price)
        holder.bind.createdAt.text = model.createdAt
        holder.bind.subPrice.text = "${model.quantity.roundToInt()} ${model.unit} - ${
            model.formatPrice(
                (model.price / model.quantity).roundToInt().toFloat()
            )
        } per ${model.unit}"
        holder.bind.title.text = model.title
        holder.bind.locText.text = model.getTrimmedLocation()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FullAdViewActivity::class.java)
            intent.putExtra("id", model.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemHolder(val bind: ItemAdHorizontalBinding) : RecyclerView.ViewHolder(bind.root) {
    }
}