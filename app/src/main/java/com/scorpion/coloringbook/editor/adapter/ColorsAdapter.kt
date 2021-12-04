package com.scorpion.coloringbook.editor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.editor.interfaces.GetItemClick
import com.scorpion.coloringbook.editor.model.EditorData
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled

class ColorsAdapter(var list: ArrayList<EditorData>, var item: GetItemClick) : RecyclerView.Adapter<ColorsAdapter.MyViewHolder>() {

    lateinit var mContext: Context
    var itemClick = item
    var lastPosition = 0

    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pencil_a = itemView.findViewById<ImageView>(R.id.pencil_a)
        var pencil_b = itemView.findViewById<ImageView>(R.id.pencil_b)
        var mainLayout = itemView.findViewById<ConstraintLayout>(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        var view = LayoutInflater.from(mContext).inflate(R.layout.item_color, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.pencil_b.setColorFilter(list[position].color, android.graphics.PorterDuff.Mode.MULTIPLY)

        holder.mainLayout.onClickThrottled {
            itemClick.onItemClick(list[position], position)
        }

        if (list[position].isSelected) {
            holder.pencil_a.translationY = -20F
            holder.pencil_b.translationY = -20F
        } else {
            holder.pencil_a.translationY = +20F
            holder.pencil_b.translationY = +20F
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}