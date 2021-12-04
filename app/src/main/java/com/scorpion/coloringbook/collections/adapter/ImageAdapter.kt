package com.scorpion.coloringbook.collections.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.constants.Constants
import com.scorpion.coloringbook.constants.Constants.Companion.SCREEN_WIDTH
import com.scorpion.coloringbook.imagepreview.ImagePreviewActivity
import java.io.File

class ImageAdapter(list: MutableList<File>) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    var myList = list
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.image_adapter_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(mContext).load(myList[position].absolutePath).into(holder.image)
        holder.image.setOnClickListener {
            mContext.startActivity(Intent(mContext, ImagePreviewActivity::class.java).putExtra("position", position))
        }

        var params = ConstraintLayout.LayoutParams(SCREEN_WIDTH, SCREEN_WIDTH)
        holder.image.layoutParams = params

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
//        var width : Int = Constants.getScreenWidth(mContext.windowManager.defaultDisplay)
    }

}