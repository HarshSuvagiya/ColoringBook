package com.scorpioninfosoft.coloringbook.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scorpioninfosoft.coloringbook.R
import com.scorpioninfosoft.coloringbook.base.BaseActivity
import com.scorpioninfosoft.coloringbook.category.adapter.CategoryAdapter
import com.scorpioninfosoft.coloringbook.category.interfaces.GetItemClick
import com.scorpioninfosoft.coloringbook.category.model.CategoryModel
import com.scorpioninfosoft.coloringbook.categorydetail.CategoryDetailActivity
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.constants.Constants.Companion.GRID_SPAN
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.CATEGORY
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.CAT_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.CAT_NAME
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.COLORING_BOOK
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.KEY
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.THUMB_URL
import com.scorpioninfosoft.coloringbook.constants.IntentConstants.Companion.CAT_MODEL
import com.scorpioninfosoft.coloringbook.constants.interfaces.AdInterface
import com.scorpioninfosoft.coloringbook.databinding.ActivityCategoryBinding
import com.scorpioninfosoft.coloringbook.extensions.Extensions.Companion.onClickThrottled
import java.util.*
import kotlin.collections.ArrayList

class CategoryActivity : BaseActivity(), GetItemClick {

    private lateinit var binding: ActivityCategoryBinding
    var db = FirebaseDatabase.getInstance().reference
    var list: ArrayList<CategoryModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        setAdapter()
        loadAd()

        binding.back.onClickThrottled {
            onBackPressed()
        }

    }

    private fun setAdapter() {
        getAllCategory()
    }

    private fun getAllCategory() {
        db.child(COLORING_BOOK).child(CATEGORY).orderByChild(CAT_ID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (item in snapshot.children) {
                    list.add(CategoryModel(item.child(CAT_ID).value.toString(),item.child(CAT_NAME).value.toString(),item.child(KEY).value.toString(),item.child(THUMB_URL).value.toString()))
                }

                list.reverse()
                var adapter = CategoryAdapter(list,this@CategoryActivity)
                binding.catRecycler.adapter = adapter
                binding.catRecycler.layoutManager = GridLayoutManager(this@CategoryActivity,GRID_SPAN)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(model: CategoryModel) {
        startActivity(Intent(applicationContext,CategoryDetailActivity::class.java).putExtra(CAT_MODEL,model))
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

}