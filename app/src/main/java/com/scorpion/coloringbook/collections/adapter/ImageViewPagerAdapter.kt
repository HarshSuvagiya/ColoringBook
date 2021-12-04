package com.scorpion.coloringbook.collections.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.scorpion.coloringbook.R
import java.io.File

class ImageViewPagerAdapter(list: MutableList<File>) : PagerAdapter() {

    private val myList = list
    private lateinit var mContext: Context

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        mContext = container.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.image_view_for_view_pager, null)
        val imageView = view.findViewById<ImageView>(R.id.image)
        Glide.with(mContext).load(myList[position].absolutePath).into(imageView)

        val viewPager = container as ViewPager
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return myList.size
    }
}