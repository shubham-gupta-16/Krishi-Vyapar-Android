package com.acoder.krishivyapar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acoder.krishivyapar.api.BaseApi
import com.acoder.krishivyapar.api.CategoryManager
import com.acoder.krishivyapar.databinding.ItemCategoryGrid2Binding
import com.bumptech.glide.Glide

class CategoryRecyclerAdapter(
    private val context: Context,
    private val list: List<CategoryManager.CategoryModel>
) :
    RecyclerView.Adapter<CategoryRecyclerAdapter.ItemHolder>() {

    private var listener : ((model: CategoryManager.CategoryModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val bind = ItemCategoryGrid2Binding.inflate(LayoutInflater.from(context))
        return ItemHolder(bind)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model = list[position]
        holder.bind.title.text = model.json.getJSONObject("name").getString("en")
        val image = BaseApi.getImagesUrl(context) + model.json.optString("thumb")
        Glide.with(context).load(image).into(holder.bind.image)
        holder.itemView.setOnClickListener {
            if (listener != null)
                listener?.invoke(model)
        }
    }

    fun onItemClickListener(listener: ((model: CategoryManager.CategoryModel) -> Unit)?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemHolder(val bind: ItemCategoryGrid2Binding) : RecyclerView.ViewHolder(bind.root) {
    }
}