package com.scorpioninfosoft.coloringbook.editor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.ads.MobileAds
import com.scorpioninfosoft.coloringbook.base.BaseActivity
import com.scorpioninfosoft.coloringbook.category.model.CategoryModel
import com.scorpioninfosoft.coloringbook.constants.AdsConstants
import com.scorpioninfosoft.coloringbook.constants.IntentConstants.Companion.CAT_MODEL
import com.scorpioninfosoft.coloringbook.databinding.ActivityEditorBinding

class EditorActivity : BaseActivity() {

    private lateinit var binding : ActivityEditorBinding
    private lateinit var model : CategoryModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        getIntentExtra()
        loadAd()
    }

    private fun getIntentExtra() {
        model = intent.getSerializableExtra(CAT_MODEL) as CategoryModel
        Glide.with(applicationContext).load(model.thumbUrl).into(binding.image)
    }

    private fun loadAd() {
        AdsConstants.loadBanner(windowManager.defaultDisplay, binding.adViewContainer, this)
    }

}