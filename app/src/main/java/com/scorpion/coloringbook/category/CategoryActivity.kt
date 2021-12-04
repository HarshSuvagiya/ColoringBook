package com.scorpion.coloringbook.category

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.category.adapter.CategoryAdapter
import com.scorpion.coloringbook.category.interfaces.GetItemClick
import com.scorpion.coloringbook.category.model.CategoryModel
import com.scorpion.coloringbook.categorydetail.CategoryDetailActivity
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.Constants.Companion.GRID_SPAN
import com.scorpion.coloringbook.constants.DBConstants.Companion.CATEGORY
import com.scorpion.coloringbook.constants.DBConstants.Companion.CAT_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.CAT_NAME
import com.scorpion.coloringbook.constants.DBConstants.Companion.COLORING_BOOK
import com.scorpion.coloringbook.constants.DBConstants.Companion.KEY
import com.scorpion.coloringbook.constants.DBConstants.Companion.THUMB_URL
import com.scorpion.coloringbook.constants.IntentConstants.Companion.CAT_MODEL
import com.scorpion.coloringbook.databinding.ActivityCategoryBinding
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled
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

        binding.topbar.title.text = getString(R.string.categories)

        binding.topbar.back.onClickThrottled {
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
                    list.add(CategoryModel(item.child(CAT_ID).value.toString(), item.child(CAT_NAME).value.toString(), item.child(KEY).value.toString(), item.child(THUMB_URL).value.toString()))
                }

                list.reverse()
                var adapter = CategoryAdapter(list, this@CategoryActivity)
                binding.catRecycler.adapter = adapter
                binding.catRecycler.layoutManager = GridLayoutManager(this@CategoryActivity, GRID_SPAN)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(model: CategoryModel) {
        startActivity(Intent(applicationContext, CategoryDetailActivity::class.java).putExtra(CAT_MODEL, model))
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

}