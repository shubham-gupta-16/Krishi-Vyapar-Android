package com.acoder.krishivyapar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.api.BaseApi
import com.acoder.krishivyapar.databinding.ItemAdHorizontalBinding
import com.acoder.krishivyapar.models.AdListModel
import com.bumptech.glide.Glide

class AdRecyclerAdapter(private val context: Context, private val list: List<AdListModel>) :
    RecyclerView.Adapter<AdRecyclerAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val bind = ItemAdHorizontalBinding.inflate(LayoutInflater.from(context))
        return ItemHolder(bind)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model = list[position]
//        todo original to thumb
        Glide.with(context).load(BaseApi.getImagesUrl(context) + model.image.getPath("original"))
            .placeholder(R.drawable.loading_background).into(holder.bind.image)
        holder.bind.price.text = model.formatPrice(model.price)
        holder.bind.title.text = model.title
        holder.bind.locText.text = model.getTrimmedLocation()
        holder.itemView.setOnClickListener {
            Toast.makeText(context, model.title, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemHolder(val bind: ItemAdHorizontalBinding) : RecyclerView.ViewHolder(bind.root) {
    }
}