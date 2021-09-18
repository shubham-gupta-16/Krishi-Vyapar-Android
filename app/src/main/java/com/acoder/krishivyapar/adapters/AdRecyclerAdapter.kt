package com.acoder.krishivyapar.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.databinding.ItemAdHorizontalBinding
import com.acoder.krishivyapar.models.AdModel

class AdRecyclerAdapter(private val context: Context, private val list: List<AdModel>) :
    RecyclerView.Adapter<AdRecyclerAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val bind = ItemAdHorizontalBinding.inflate(LayoutInflater.from(context))
        return ItemHolder(bind)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model = list[position]
        holder.bind.price.text = model.price.toString()
        holder.bind.title.text = model.title
        holder.bind.locText.text = model.location.getTrimmed()
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