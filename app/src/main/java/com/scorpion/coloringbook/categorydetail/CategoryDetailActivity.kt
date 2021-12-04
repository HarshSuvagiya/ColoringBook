package com.scorpion.coloringbook.categorydetail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.scorpion.coloringbook.R
import com.scorpion.coloringbook.base.BaseActivity
import com.scorpion.coloringbook.category.interfaces.GetItemClick
import com.scorpion.coloringbook.category.model.CategoryModel
import com.scorpion.coloringbook.categorydetail.adapter.CategoryDetailAdapter
import com.scorpion.coloringbook.constants.AdsConstants
import com.scorpion.coloringbook.constants.Constants.Companion.GRID_SPAN
import com.scorpion.coloringbook.constants.DBConstants.Companion.CAT_ID
import com.scorpion.coloringbook.constants.DBConstants.Companion.CAT_NAME
import com.scorpion.coloringbook.constants.DBConstants.Companion.COLORING_BOOK
import com.scorpion.coloringbook.constants.DBConstants.Companion.DATA
import com.scorpion.coloringbook.constants.DBConstants.Companion.KEY
import com.scorpion.coloringbook.constants.DBConstants.Companion.THUMB_URL
import com.scorpion.coloringbook.constants.IntentConstants.Companion.CAT_MODEL
import com.scorpion.coloringbook.databinding.ActivityCategoryDetailBinding
import com.scorpion.coloringbook.editor.EditorActivity
import com.scorpion.coloringbook.extensions.Extensions.Companion.onClickThrottled

class CategoryDetailActivity : BaseActivity(),GetItemClick {

    private lateinit var binding: ActivityCategoryDetailBinding
    private lateinit var model : CategoryModel
    var db = FirebaseDatabase.getInstance().reference
    var list: ArrayList<CategoryModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        getIntentExtra()
        loadAd()
        binding.topbar.title.text = model.catName

        binding.topbar.back.onClickThrottled {
            onBackPressed()
        }
    }

    private fun getIntentExtra() {
        model = intent.getSerializableExtra(CAT_MODEL) as CategoryModel
        getDataCatWise()
    }

    private fun getDataCatWise() {
        db.child(COLORING_BOOK).child(DATA).orderByChild(CAT_ID).equalTo(model.catId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (item in snapshot.children) {
                    list.add(CategoryModel(item.child(CAT_ID).value.toString(),item.child(CAT_NAME).value.toString(),item.child(KEY).value.toString(),item.child(THUMB_URL).value.toString()))
                }

                list.reverse()
                var adapter = CategoryDetailAdapter(list,this@CategoryDetailActivity)
                binding.catDetailRecycler.adapter = adapter
                binding.catDetailRecycler.layoutManager = GridLayoutManager(this@CategoryDetailActivity,GRID_SPAN)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(model: CategoryModel) {
        startActivity(Intent(applicationContext,EditorActivity::class.java).putExtra(CAT_MODEL,model))
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

}