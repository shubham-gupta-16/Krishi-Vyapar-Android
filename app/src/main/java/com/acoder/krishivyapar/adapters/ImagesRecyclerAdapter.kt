package com.acoder.krishivyapar.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acoder.krishivyapar.R
import com.acoder.krishivyapar.databinding.ItemImageSelectBinding
import com.bumptech.glide.Glide
import kotlin.math.log

class ImagesRecyclerAdapter(
    private val context: Context,
    private val list: List<Uri>
) : RecyclerView.Adapter<ImagesRecyclerAdapter.ItemHolder>() {

    data class Select(var value: Int, val position: Int)

    private val selectList = HashMap<String, Select>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val bind = ItemImageSelectBinding.inflate(LayoutInflater.from(context))
        return ItemHolder(bind)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val uri = list[position].toString()

        if (selectList.containsKey(uri)) {
            val count = selectList[uri] as Select
            holder.bind.selectedText.text = count.value.toString()
            holder.bind.selectedText.setBackgroundResource(R.drawable.background_badge_image_select_active)
            holder.bind.blur.alpha = 1f
        } else {
            holder.bind.selectedText.text = ""
            holder.bind.selectedText.setBackgroundResource(R.drawable.background_badge_image_select)
            holder.bind.blur.alpha = 0f
        }

        Glide.with(context).load(uri).thumbnail(0.5f).centerCrop().into(holder.bind.image)
        holder.itemView.setOnClickListener {
            if (selectList.containsKey(uri)) {
                unselect(uri)
            } else
                select(uri, position)
        }
    }

    private fun unselect(uri: String) {
        val removed = selectList[uri] as Select
        var minRange = -1;
        var maxRange = -1;
        for ((key, select) in selectList) {
            if (select.value > removed.value) {
                selectList[key] = Select(select.value - 1, select.position)
            }
            if (select.position > maxRange)
                maxRange = select.position
            if (select.position < minRange || minRange == -1)
                minRange = select.position
        }
        selectList.remove(uri)
        notifyItemRangeChanged(minRange, maxRange - minRange + 2)
//        notifyDataSetChanged()
    }

    private fun select(uri: String, position: Int) {
        val count = selectList.size + 1
        selectList[uri] = Select(count, position)
        notifyItemChanged(position)
    }

    fun getImages(): Map<String, Int> {
        val list = HashMap<String, Int>()
        for ((key, select) in selectList){
            list[key] = select.value
        }
        return list
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemHolder(val bind: ItemImageSelectBinding) : RecyclerView.ViewHolder(bind.root) {
    }
}