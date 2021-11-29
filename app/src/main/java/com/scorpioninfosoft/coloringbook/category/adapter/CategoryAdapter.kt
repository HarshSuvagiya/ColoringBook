package com.scorpioninfosoft.coloringbook.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scorpioninfosoft.coloringbook.R
import com.scorpioninfosoft.coloringbook.category.interfaces.GetItemClick
import com.scorpioninfosoft.coloringbook.category.model.CategoryModel
import com.scorpioninfosoft.coloringbook.extensions.Extensions.Companion.onClickThrottled

class CategoryAdapter(var list: ArrayList<CategoryModel>,var item: GetItemClick) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    lateinit var mContext: Context
    var itemClick = item

    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.image)
        var catName = itemView.findViewById<TextView>(R.id.catName)
        var mainLayout = itemView.findViewById<ConstraintLayout>(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        var view = LayoutInflater.from(mContext).inflate(R.layout.item_cat, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(mContext).load(list[position].thumbUrl).into(holder.image)
        holder.catName.text = list[position].catName

        holder.mainLayout.onClickThrottled {
            itemClick.onItemClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}