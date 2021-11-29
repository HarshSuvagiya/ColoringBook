package com.scorpioninfosoft.coloringbook.categorydetail

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
import com.scorpioninfosoft.coloringbook.categorydetail.adapter.CategoryDetailAdapter
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.constants.Constants.Companion.GRID_SPAN
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.CAT_ID
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.CAT_NAME
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.COLORING_BOOK
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.DATA
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.KEY
import com.scorpioninfosoft.coloringbook.constants.DBConstants.Companion.THUMB_URL
import com.scorpioninfosoft.coloringbook.constants.IntentConstants.Companion.CAT_MODEL
import com.scorpioninfosoft.coloringbook.databinding.ActivityCategoryDetailBinding
import com.scorpioninfosoft.coloringbook.editor.EditorActivity
import com.scorpioninfosoft.coloringbook.extensions.Extensions.Companion.onClickThrottled

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
        binding.title.text = model.catName

        binding.back.onClickThrottled {
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