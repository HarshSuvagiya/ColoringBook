package com.scorpion.coloringbook.categorydetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.category.interfaces.GetItemClick
import com.scorpion.coloringbook.category.model.CategoryModel
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled

class CategoryDetailAdapter (var list: ArrayList<CategoryModel>, var item: GetItemClick) : RecyclerView.Adapter<CategoryDetailAdapter.MyViewHolder>() {

    lateinit var mContext: Context
    var itemClick = item

    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<ImageView>(R.id.image)
        var mainLayout = itemView.findViewById<ConstraintLayout>(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        var view = LayoutInflater.from(mContext).inflate(R.layout.item_cat_detail, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(mContext).load(list[position].thumbUrl).into(holder.image)

        holder.mainLayout.onClickThrottled {
            itemClick.onItemClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}